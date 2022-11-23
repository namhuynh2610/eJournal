import {
  LOADING,
  LOADING_ALERT,
  SET_EDIT_REVIEW,
  SUCCESS_NO_MESSAGE,
  CLEAR_REVIEW_VALUES,
  ERROR,
  SUCCESS,
} from "../actions";
import { clearAlert, handleChange } from "./utilService";
import authFetch from "../../utils/authFetch";

export const getReviewReport =
  ({ title, verdict, page, status }) =>
  async (dispatch) => {
    dispatch({ type: LOADING });
    try {
      const { data } = await authFetch.post("/reviewreport/search", {
        title,
        verdict: verdict === "ALL" ? null : verdict,
        page,
        status: status === "ALL" ? null : status,
      });
      dispatch({ type: SUCCESS_NO_MESSAGE });
      dispatch(
        handleChange({
          name: "reviewReports",
          value: data,
          type: "reviewer_spread_searchreview",
        })
      );
    } catch (error) {
      if (error.response.status === 401) return;
      dispatch({
        type: ERROR,
        payload: { msg: error.response.data.message },
      });
    }
    dispatch(clearAlert());
  };

export const getReviewReportDetail =
  ({ reviewId }) =>
  async (dispatch) => {
    dispatch({ type: LOADING });
    try {
      const { data } = await authFetch.get(
        `/reviewer/reviewreport/${reviewId}`
      );
      dispatch({ type: SUCCESS_NO_MESSAGE });
      dispatch(
        handleChange({ name: "reviewDetail", value: data, type: "reviewer" })
      );
    } catch (error) {
      if (error.response.status === 401) return;
      dispatch({
        type: ERROR,
        payload: { msg: error.response.data.message },
      });
    }
    dispatch(clearAlert());
  };

export const editReview = (review) => async (dispatch) => {
  dispatch({ type: LOADING_ALERT });
  try {
    const {
      editReviewId,
      reviewNote,
      reviewGrade,
      reviewConfidentiality,
      reviewVerdict,
    } = review;

    const data = {
      grade: reviewGrade,
      confidentiality: reviewConfidentiality,
      note: reviewNote,
      verdict: reviewVerdict,
    };
    await authFetch.put(`/reviewreport/${editReviewId}`, data);
    dispatch({
      type: SUCCESS,
      payload: { msg: "Submit review successfully" },
    });
    dispatch({ type: CLEAR_REVIEW_VALUES });
  } catch (error) {
    if (error.response.status === 401) return;
    dispatch({
      type: ERROR,
      payload: { msg: error.response.data.message },
    });
  }
  dispatch(clearAlert());
};

export const setEditReview = (id) => (dispatch) => {
  // dispatch({ type: CLEAR_REVIEW_VALUES });
  dispatch({ type: SET_EDIT_REVIEW, payload: { id } });
};

export const clearReviewValues = () => (dispatch) => {
  dispatch({ type: CLEAR_REVIEW_VALUES });
};
