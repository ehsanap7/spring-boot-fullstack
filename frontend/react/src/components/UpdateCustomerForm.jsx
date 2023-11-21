import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {updateCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

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

const MySelect = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2} mb={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const UpdateCustomerForm = ({customer, fetchCustomers}) => {
    return (
        <>
            <Formik
                initialValues={{
                    id: customer.id,
                    name: customer.name,
                    email: customer.email,
                    age: customer.age,
                    gender: customer.gender,
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(16, 'Must be at least 16')
                        .max(100, 'Must be less than 100')
                        .required('Required'),
                    gender: Yup.string()
                        .oneOf(
                            ['MALE', 'FEMALE'],
                            'Invalid Gender Type'
                        )
                        .required('Required'),
                })}
                onSubmit={(customers, {setSubmitting}) => {
                    setSubmitting(true);
                    updateCustomer(customers).then(res => {
                        console.log(res);
                        successNotification(
                            'Customer updated',
                            `${customers.name} updated`
                        )
                        fetchCustomers();
                    }).catch(err => {
                        console.log(err);
                        errorNotification(
                            err.code,
                            err.response.data
                        );
                    }).finally(() => {
                        setSubmitting(false);
                    });
                }}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@formik.com"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="20"
                            />

                            <MySelect label="Gender" name="gender">
                                <option value="">Select a gender type</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </MySelect>

                            <Button isDisabled={!(isValid && dirty) || isSubmitting} type="submit">Update</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateCustomerForm;