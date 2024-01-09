'use client'

import {
    Button,
    Checkbox,
    Flex,
    Text,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image, Link, Box, Alert, AlertIcon,
} from '@chakra-ui/react'
import {Formik, Form, useField} from "formik";
import * as Yup from 'yup';

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2} mb={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const LoginForm = () => {
    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .max(20, "Password can not be more than 20 character")
                        .required("Password is required")
                })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                alert(JSON.stringify(values, null, 0));
            }}>

            {(isValid, isSubmitting) => {
                return (
                    <Form>
                        <Stack spacing={15}>
                            <MyTextInput
                                label={"Email"}
                                name={"username"}
                                type={"email"}
                                placeholder={"hello@ehsan.com"}
                            />
                            <MyTextInput
                                label={"Password"}
                                name={"password"}
                                type={"password"}
                                placeholder={"Type your Password"}
                            />
                            <Button
                                type={"submit"}
                                disabled={!isValid || isSubmitting}>
                                Login
                            </Button>
                        </Stack>
                    </Form>
                )
            }}

        </Formik>
    );
}

const Login = () => {
    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={10} w={'full'} maxW={'md'}>
                    <Image
                        src={'https://st5.depositphotos.com/69915036/62675/v/450/depositphotos_626754468-stock-illustration-your-logo-here-placeholder-symbol.jpg'}
                        boxSize={"200px"}
                        alt={"Ehsan Ap Logo"}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                    <LoginForm/>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={10}
                flexDirection={"column"}
                align={"center"}
                justifyContent={"center"}
                bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}
            >
                <Text fontSize={"6xl"} color={"white"}
                      fontWeight={"bold"} mb={5}>
                    <Link href={"https://google.com"}>
                        Enrol Now
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1352&q=80'
                    }
                />
            </Flex>
        </Stack>
    )
}
export default Login;