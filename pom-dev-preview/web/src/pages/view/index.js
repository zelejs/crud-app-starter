import React, { useState, useEffect } from 'react';
import {
    ChakraProvider, Box, VStack, Spinner, Button, FormControl, FormLabel
} from "@chakra-ui/react";
import { history } from "umi";

import { AutoLayout } from 'zero-element-boot/lib/components';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import AddNewBtn from '@/components/Presenter/AddNewButton';

require('./index.less')


export default function Index(props) {


    function addComponent() {
        history.push('/presenters')
    }

    return (
        <ChakraProvider>
            <VStack align='stretch' spacing='-2'>
                <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                    <FormControl display='flex' alignItems='center'>
                        <Button colorScheme='teal' size='sm'>
                            返回
                        </Button>
                    </FormControl>
                </Box>

                <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
                    <AutoLayout/>
                </Box>

                <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
                    <a onClick={() => addComponent()}>
                        <AddNewBtn/>
                    </a>
                </Box>
            </VStack>
        </ChakraProvider>
    )
}