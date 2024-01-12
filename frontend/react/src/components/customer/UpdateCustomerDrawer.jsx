import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";
import React from "react";

const AddIcon = () => "+";

const UpdateCustomerDrawer = ({customer, fetchCustomers}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            onClick={onOpen}
            bg={'green.400'}
            color={'white'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            _focus={{
                bg: 'green.500'
            }}
        >
            Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Update customer {customer.name}</DrawerHeader>
                <DrawerBody>
                    <UpdateCustomerForm
                        customer={customer}
                        fetchCustomers={fetchCustomers}
                    />
                </DrawerBody>
                <DrawerFooter>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>

}

export default UpdateCustomerDrawer;
