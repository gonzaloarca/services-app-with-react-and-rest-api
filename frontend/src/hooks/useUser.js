import {
  getUserByEmailRequest,
  loginRequest,
  getUserByIdRequest,
  registerRequest,
  verifyEmailRequest,
  getRankingsRequest,
  getProfessionalInfoRequest,
} from '../api/usersApi';
const useUserHook = () => {
  const getUserByEmail = async (email) => {
    const response = await getUserByEmailRequest(email);
    return response.data;
  };

  const login = async ({ email, password }) => {
    const response = await loginRequest({ email, password });
    return response.headers.authorization;
  };

  const getUserById = async (id) => {
    const response = await getUserByIdRequest(id);
    return response.data;
  };

  const register = async (newUser) => {
    const response = await registerRequest({
      ...newUser,
      webPageUrl: process.env.REACT_APP_PAGE_URL + 'token',
    });
    return response.data;
  };

  const verifyEmail = async (data) => {
    const response = await verifyEmailRequest(data);
    return response.data;
  };

  const getRankings = async (userId) => {
    const response = await getRankingsRequest(userId);
    return response.data;
  };

  const getProfessionalInfo = async (userId) => {
    const response = await getProfessionalInfoRequest(userId);
    return response.data;
  };

  return {
    getUserByEmail,
    getUserById,
    login,
    register,verifyEmail,
    getRankings,
    getProfessionalInfo,
  };
};
export default useUserHook;
