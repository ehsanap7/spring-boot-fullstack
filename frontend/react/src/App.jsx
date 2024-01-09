import {Button, Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/customer/Card.jsx";
import CreateCustomerDrawer from "./components/customer/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true);
        setTimeout(() => {
            getCustomers().then((resp) => {
                setCustomers(resp.data)
                console.log(resp);
            }).catch(err => {
                console.log(err);
                setError(err.response.data.message);
                errorNotification(
                    err.code,
                    err.response.data.message
                );
            }).finally(() => {
                setLoading(false);
            })
        }, 1);
    }

    useEffect(() => {
        fetchCustomers();
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if(err){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={2}> There was an error</Text>
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={2}> No Customers Available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer fetchCustomers={fetchCustomers}/>
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                        <WrapItem key={index}>
                            <CardWithImage
                                customer= {customer}
                                fetchCustomers={fetchCustomers}
                            />
                        </WrapItem>
                    )
                )}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;