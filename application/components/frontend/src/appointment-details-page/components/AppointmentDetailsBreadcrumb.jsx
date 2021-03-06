import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';

import printIcon from 'images/icons/print.svg';

const AppointmentDetailsBreadcrumbWrapper = styled.div.attrs({
  className: 'appointment-details-breadcrumb-wrapper',
})`
  width: 100%;
  font-size: 11px;
  padding-bottom: 5px;
  margin-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
`;

const LeftSide = styled.span.attrs({ className: 'left-side' })`
  line-height: 30px;
`;

const RightSide = styled.span.attrs({ className: 'right-side' })`
  text-align: right;
`;

const PrintIcon = styled.img.attrs({ className: 'print-icon' })`
  width: 15px;
  background: #ffffff;
  padding: 5px;
  border-radius: 5px;
  border: 1px solid #f0f0f0;
  cursor: pointer;
`;

const PageTitle = styled.span.attrs({ className: 'page-title' })`
  font-weight: 400;
  display: inline-block;
  margin-right: 3px;
`;

const PatientName = styled.span.attrs({ className: 'page-name' })`
  font-weight: 100;
  display: inline-block;
`;

const AppointmentDetailsBreadcrumb = ({ patientName }) => {
  return (
    <AppointmentDetailsBreadcrumbWrapper>
      <LeftSide>
        <PageTitle>Appointment Details &gt;</PageTitle>
        <PatientName>{patientName}</PatientName>
      </LeftSide>
      <RightSide>
        <PrintIcon src={printIcon} alt="print-icon" />
      </RightSide>
    </AppointmentDetailsBreadcrumbWrapper>
  );
};

AppointmentDetailsBreadcrumb.propTypes = {
  patientName: PropTypes.string.isRequired,
};

export default AppointmentDetailsBreadcrumb;
