import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => "+";

const CreateCustomerDrawer = ({ fetchCustomers }) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button colorScheme={"teal"} leftIcon={<AddIcon/>} onClick={onOpen} >
            Create New Customer For This Project
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Create new customer</DrawerHeader>
                <DrawerBody>
                    <CreateCustomerForm
                    fetchCustomers={ fetchCustomers }
                    />
                </DrawerBody>
                <DrawerFooter>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>

}

export default CreateCustomerDrawer;
