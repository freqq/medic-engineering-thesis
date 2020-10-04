#!/bin/bash

source common/logger.sh

set -e

SOURCE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CA_CERTS_FOLDER=$(pwd)/.certs
MINIKUBE_IP=$(minikube ip)

function enable_ingres_on_minikube() (
    minikube addons enable ingress
)

function generate_secrets() {
    log_info "Creating self-signed CA certificates for TLS and installing them in the local trust stores"

    rm -rf ${CA_CERTS_FOLDER}
    mkdir -p ${CA_CERTS_FOLDER}
    mkdir -p ${CA_CERTS_FOLDER}/${ENVIRONMENT_DEV}
    CAROOT=${CA_CERTS_FOLDER}/${ENVIRONMENT_DEV} mkcert -install

    log_info "Creating K8S secrets with the CA private keys (will be used by the cert-manager CA Issuer)"
    kubectl -n cert-manager create secret tls my-ca-tls-secret --key=${CA_CERTS_FOLDER}/${ENVIRONMENT_DEV}/rootCA-key.pem --cert=${CA_CERTS_FOLDER}/${ENVIRONMENT_DEV}/rootCA.pem

    log_info "Secrets generated."
}

function setup_cert_manager() {
    log_info "Setting up cert-manager..."

    cd ${SOURCE_DIR}/../application

    ./gradlew charts:cert-manager-namespace:appInstall
    generate_secrets

    kubectl apply --validate=false -f \
        https://github.com/jetstack/cert-manager/releases/download/v1.0.2/cert-manager.crds.yaml

    ./gradlew charts:cert-manager:appInstall
    ./gradlew charts:cert-manager-config:appInstall

    cd ../scripts

    log_info "Cert-manager up and running."
}

function add_domain_to_hosts() {
    log_info "Adding domain to hosts file ..."

    sudo sed -i '' '/docwire\.test/d' /etc/hosts
    echo "${MINIKUBE_IP} docwire.test" | sudo tee -a /etc/hosts

    log_info "Domain added."
}

function build_custom_images() {
    log_info "Staring to build custom images ..."

    ./gradlew charts:auth-service:appLoad
    ./gradlew charts:rabbit-mq:appLoad

    log_info "Custom images built."
}

function app_start() (
    log_info "Starting DocWIRE..."

    cd ${SOURCE_DIR}/../application

    ./gradlew charts:namespace:appInstall

    ./gradlew charts:auth-db:appInstall
    ./gradlew charts:auth-service:appInstall

    ./gradlew charts:account-db:appInstall
    ./gradlew charts:account-service:appLoad
    ./gradlew charts:account-service:appInstall

    ./gradlew charts:rabbit-mq:appInstall

    ./gradlew charts:messages-db:appInstall
    ./gradlew charts:messages-service:appLoad
    ./gradlew charts:messages-service:appInstall

    ./gradlew charts:openvidu-redis:appInstall
    ./gradlew charts:openvidu-coturn:appInstall
    ./gradlew charts:openvidu-server:appInstall

    ./gradlew charts:appointments-db:appInstall
    ./gradlew charts:appointments-service:appLoad
    ./gradlew charts:appointments-service:appInstall

    # minikube mount application/components/frontend:/frontend/src

    ./gradlew charts:frontend:appLoad
    ./gradlew charts:frontend:appInstall

    cd -
    log_info "DocWIRE started."
)

function main() {
    build_custom_images

    enable_ingres_on_minikube
    add_domain_to_hosts
    setup_cert_manager
    app_start
}

main