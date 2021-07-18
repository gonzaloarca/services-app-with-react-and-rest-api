import hirenetApi from "./hirenetApi";

export const getJobCardsRequest = (page = 1) => {
  return hirenetApi.get("/job-cards",{
    params:{
      page: page
    }
  })  
}

export const searchJobCardsRequest = ({zone,query,category,orderBy,page = 1}) => {
  return hirenetApi.get("/job-cards/search",{
    params:{
      zone: zone,
      query: query,
      category: category,
      orderBy: orderBy,
      page: page
    }
  })
}

export const getOrderByParamsRequest = () =>{
  return hirenetApi.get("/job-cards/order-params");
}

export const getOrderByParamByIdRequest = (id) =>{
  return hirenetApi.get(`/job-cards/order-params/${id}`);
}