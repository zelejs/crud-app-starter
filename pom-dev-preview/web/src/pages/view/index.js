import React, { useState, useEffect } from 'react';
import {
    ChakraProvider, Box, VStack, Spinner, Button, FormControl, FormLabel
} from "@chakra-ui/react";
import { history } from "umi";

import { AutoLayout } from 'zero-element-boot/lib/components';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import AddNewBtn from '@/components/Presenter/AddNewButton';

require('./index.less')


const routeMap = {
    presenter: '/presenters',
    cart: '/carts',
    layout: '/layouts',
    container: '/containers',
}

export default function Index(props) {

    const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 

    const [ layoutConfig, setLayoutConfig ] = useState({})
    const [ nextComponent, setNextComponent ] = useState('')
    const [isLoading, setLoading] = useState(false);


    useEffect(_=>{
        getLayoutData()
        // const layout = {
        //     "layoutJson": null,
        //     "nextComponent": "presenter"
        // }
        // setLayoutConfig(layout.layoutJson)
        // setNextComponent(layout.nextComponent)
        // console.log('layoutConfig = ', layoutConfig, nextComponent)
    },[])

    function getLayoutData() {
        setLoading(true)
        let api = '/openapi/lc/module/build-auto-layout/' + id
        const queryData = {};
        promiseAjax(api, queryData, { method: 'PATCH'}).then(resp => {
            setLoading(false)
            if (resp && resp.code === 200) {
                // console.log('layout resp = ', resp)
                setLayoutConfig(resp.data.layoutJson)
                setNextComponent(resp.data.nextComponent)
            } else {
                console.error("获取页面数据失败")
            }
        });
    }
    function addComponent() {
        const path = routeMap[nextComponent]
        history.push({
            pathname: path,
            query: {
                id
            }
        })
    }

    const config = {
        items: [{id:1}],
        layout: layoutConfig,
        // layout: 
        // {
        //     presenter:{
        //         xname: 'Avatar',
        //         props:{
        //             size: '50'
        //         }
        //     },
        //     cart: {
        //         xname: 'Cart',
        //         props: {
        //           padding: '10px',
        //           margin: '1px 0',
        //           linewidth: 0,
        //           corner: '8px',
        //         }
        //     },
        //     xname: 'Flexbox',
        //     props: {
        //         align: 'start',
        //         direction: 'row',
        //         justify: 'center',
        //     },
        //     container: 'PlainList'
        // } 
    }

    return (
        <ChakraProvider>
            <VStack align='stretch' spacing='-2'>
                <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                    <FormControl display='flex' alignItems='center'>
                        <Button colorScheme='teal' size='sm' onClick={() => history.push('/')}>
                            返回
                        </Button>
                    </FormControl>
                </Box>

                <Box style={{ margin: '10px 5px 10px 5px', paddingLeft: '8px' }} >
                
                {/* <AutoLayout {...config}/> */}
                
                {
                    isLoading
                    ? <Spinner/>
                    : layoutConfig && layoutConfig.presenter && <AutoLayout {...config}/>
                }
                    
                </Box>

                <Box style={{ margin: '10px 5px 10px 5px', paddingLeft: '8px', width: '40px' }} >
                    <a onClick={() => addComponent()}>
                        <AddNewBtn/>
                    </a>
                </Box>

            </VStack>
        </ChakraProvider>
    )
}