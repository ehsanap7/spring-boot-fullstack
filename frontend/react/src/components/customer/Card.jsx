'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Button,
    useColorModeValue,
    Tag,
    AlertDialogOverlay,
    AlertDialogContent,
    AlertDialogHeader,
    AlertDialogBody,
    AlertDialog,
    AlertDialogFooter, useDisclosure,
} from '@chakra-ui/react'
import {deleteCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";
import React from "react";
import CreateCustomerDrawer from "./CreateCustomerDrawer.jsx";
import UpdateCustomerDrawer from "./UpdateCustomerDrawer.jsx";

export default function CardWithImage({customer, fetchCustomers}) {

    const {isOpen, onOpen, onClose} = useDisclosure()
    const cancelRef = React.useRef()

    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${customer.gender === 'MALE' ? 'men' : 'women'}/${customer.id}.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={3} align={'center'} mb={5}>
                        <Tag>{customer.id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {customer.name}
                        </Heading>
                        <Text color={'gray.500'}>Email is {customer.email}</Text>
                        <Text
                            color={'gray.500'}> {customer.age} | {customer.gender.charAt(0).toUpperCase() + customer.gender.slice(1).toLowerCase()}</Text>
                    </Stack>
                    <Stack  mt={20} mb={4} direction={"row"} justify={"center"} spacing={6}>
                        <Stack>
                            <UpdateCustomerDrawer customer={customer} fetchCustomers={fetchCustomers}/>
                        </Stack>
                        <Stack>
                            <Button
                                onClick={onOpen}
                                bg={'red.400'}
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
                                Delete
                            </Button>
                            <AlertDialog
                                isOpen={isOpen}
                                leastDestructiveRef={cancelRef}
                                onClose={onClose}
                            >
                                <AlertDialogOverlay>
                                    <AlertDialogContent>
                                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                            Delete Customer
                                        </AlertDialogHeader>

                                        <AlertDialogBody>
                                            Are you sure you want to delete {customer.name}? You can't undo this action
                                            afterwards.
                                        </AlertDialogBody>

                                        <AlertDialogFooter>
                                            <Button ref={cancelRef} onClick={onClose}>
                                                Cancel
                                            </Button>
                                            <Button colorScheme='red' onClick={() => {
                                                deleteCustomer(customer.id).then(resp => {
                                                    console.log(resp);
                                                    successNotification(
                                                        'Customer deleted'
                                                        , `${customer.name} is deleted`
                                                    )
                                                    fetchCustomers();
                                                }).catch(err => {
                                                    console.log(err);
                                                    errorNotification(
                                                        err.code,
                                                        err.response.data.message
                                                    );
                                                }).finally(() => {
                                                    onClose();
                                                })
                                            }} ml={3}>
                                                Delete
                                            </Button>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>
                                </AlertDialogOverlay>
                            </AlertDialog>
                        </Stack>
                    </Stack>
                </Box>
            </Box>
        </Center>
    )
}