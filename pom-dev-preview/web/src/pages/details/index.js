import React, { useState, useEffect } from 'react';
import {
    ChakraProvider, Box, VStack, Spinner, Button, FormControl, FormLabel
} from "@chakra-ui/react";
import { history } from "umi";

import { AutoLayout } from 'zero-element-boot/lib/components';
// import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import { HCenter } from 'zero-element-boot/lib/components/cart';
import { layoutJson, layoutConverter } from './config'

require('./index.less')


const routeMap = {
    presenter: '/presenters',
    cart: '/carts',
    layout: '/layouts',
    container: '/containers',
}

export default function Index(props) {

    const { componentId, layoutName } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 

    const [ layoutConfig, setLayoutConfig ] = useState({})
    const [ nextComponent, setNextComponent ] = useState('')
    const [isLoading, setLoading] = useState(false);


    useEffect(_=>{
        // getLayoutData()
        // const layout = {
        //     "layoutJson": null,
        //     "nextComponent": "presenter"
        // }
        // setLayoutConfig(layout.layoutJson)
        // setNextComponent(layout.nextComponent)
        // console.log('layoutConfig = ', layoutConfig, nextComponent)
    },[])

    // function getLayoutData() {
    //     setLoading(true)
    //     let api = '/openapi/lc/module/build-auto-layout/' + id
    //     const queryData = {};
    //     promiseAjax(api, queryData, { method: 'PATCH'}).then(resp => {
    //         setLoading(false)
    //         if (resp && resp.code === 200) {
    //             // console.log('layout resp = ', resp)
    //             setLayoutConfig(resp.data.layoutJson)
    //             setNextComponent(resp.data.nextComponent)
    //         } else {
    //             console.error("获取页面数据失败")
    //         }
    //     });
    // }


    function handleLayoutChange() {
        
        return <AutoLayout layout={layoutJson} componentId={componentId} />
    }

    return (
        <ChakraProvider>
            <HCenter>
                {handleLayoutChange()}
            </HCenter>
        </ChakraProvider>
    )
}