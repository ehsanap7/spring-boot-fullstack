import axios from "axios";

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('access_token')}`
    }
});

export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`,
            getAuthConfig())
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/insert`
            , customer
        );
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/update`
            , customer
            , getAuthConfig()
        );
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (customerId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/delete/${customerId}`,
            getAuthConfig()
        );
    } catch (e) {
        throw e;
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
            usernameAndPassword
        );
    } catch (e) {
        throw e;
    }
}