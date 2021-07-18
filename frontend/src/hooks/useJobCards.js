import { useEffect, useState } from 'react';
import {
  getJobCardsRequest,
  getOrderByParamsRequest,
  searchJobCardsRequest,
} from '../api/jobCardsApi';
import parse from 'parse-link-header';
import useCategoriesHook from './useCategories';
const useJobCardsHook = () => {
  const { categoryImageMap } = useCategoriesHook();
  const initialLinks = {
    last: {
      page: 1,
      rel: 'last',
      url: null,
    },
    next: {
      page: 1,
      rel: 'next',
      url: null,
    },
    prev: {
      page: 1,
      rel: 'prev',
      url: null,
    },
    first: {
      page: 1,
      rel: 'first',
      url: null,
    },
  };
  const [links, setLinks] = useState({ ...initialLinks });

  const getJobCards = async (page) => {
    const response = await getJobCardsRequest(page);
    setLinks(parse(response.headers.link) || { ...initialLinks });
    return response.data.map((jobCard) => ({
      ...jobCard,
      imageUrl: jobCard.imageUrl || categoryImageMap(jobCard.jobType.id),
    }));
  };

  const searchJobCards = async (queryParams) => {
    const response = await searchJobCardsRequest(queryParams);
    setLinks(parse(response.headers.link) || { ...initialLinks });
    return response.data.map((jobCard) => ({
      ...jobCard,
      imageUrl: jobCard.imageUrl || categoryImageMap.get(jobCard.jobType.id),
    }));
  };

  const getOrderByParams = async () => {
    const response = await getOrderByParamsRequest();
    return response.data;
  };

  const getOrderByParamById = async (id) => {
    const response = await getOrderByParamsRequest(id);
    return response.data;
  };

  return {
    getJobCards,
    searchJobCards,
    getOrderByParams,
    getOrderByParamById,
    links,
  };
};
export default useJobCardsHook;
