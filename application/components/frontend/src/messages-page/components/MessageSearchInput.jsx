import React, { useState } from 'react';
import styled from 'styled-components';

import searchIcon from 'images/icons/search_left.svg';

const MessageSearchInputWrapper = styled.div.attrs({ className: 'message-serach-input-wrapper' })`
  position: relative;
  margin-top: 10px;
  display: inline-block;
  width: 60%;

  @media only screen and (max-width: 1130px) {
    width: 40%;
  }

  @media only screen and (max-width: 990px) {
    display: none;
  }
`;

const MessageSearchInputComponent = styled.input.attrs({
  className: 'message-search-input-component',
})`
  border-radius: 5px;
  background: #f5f7f9;
  color: #cbced0;
  font-weight: 300;
  padding: 15px 15px 15px 40px;
  border: 1px solid #f0f0f0;
  outline: none;
  color: #000000;
  font-size: 10px;
  width: 70%;
  margin-left: 10px;
`;

const MessagesSearchIcon = styled.img.attrs({
  className: 'messages-search-icon',
  alt: 'messages-search-icon',
})`
  width: 16px;
  height: 16px;
  position: absolute;
  left: 20px;
  top: 12px;
`;

const PLACEHOLDER = 'Search for messages...';

const MessageSearchInput = () => {
  const [searchInput, setSearchInput] = useState('');

  const onChange = event => {
    const {
      target: { value },
    } = event;

    setSearchInput(value);
  };

  return (
    <MessageSearchInputWrapper>
      <MessagesSearchIcon src={searchIcon} />
      <MessageSearchInputComponent
        placeholder={PLACEHOLDER}
        onChange={onChange}
        value={searchInput}
      />
    </MessageSearchInputWrapper>
  );
};

export default MessageSearchInput;
