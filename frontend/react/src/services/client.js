import axios from "axios";

export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`)
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/insert`
            ,customer
        );
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/update`
            ,customer
        );
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (customerId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/delete/${customerId}`,
        );
    } catch (e) {
        throw e;
    }
}