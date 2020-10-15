import {
  FETCH_APPOINTMENTS_LIST_PENDING,
  FETCH_APPOINTMENTS_LIST_OK,
  FETCH_APPOINTMENTS_LIST_FAIL,
} from 'appointments-page/actions/appointmentActions';

const INITIAL_STATE = {
  data: undefined,
  isLoading: true,
  isError: false,
};

const fetchPending = state => ({
  ...state,
  data: undefined,
  isError: false,
  isLoading: true,
});

const fetchFail = state => ({
  ...state,
  data: undefined,
  isError: true,
  isLoading: false,
});

const fetchOk = (state, action) => ({
  ...state,
  data: action.payload.appointmentData,
  isError: false,
  isLoading: false,
});

export default (state, action) => {
  const stateDefinition = typeof state === 'undefined' ? INITIAL_STATE : state;
  switch (action.type) {
    case FETCH_APPOINTMENTS_LIST_OK:
      return fetchOk(stateDefinition, action);
    case FETCH_APPOINTMENTS_LIST_PENDING:
      return fetchPending(stateDefinition);
    case FETCH_APPOINTMENTS_LIST_FAIL:
      return fetchFail(stateDefinition);
    default:
      return stateDefinition;
  }
};
