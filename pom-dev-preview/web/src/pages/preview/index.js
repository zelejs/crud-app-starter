import React, { useState, useEffect } from 'react';
import {
    ChakraProvider, Box, VStack, Spinner, Button, FormControl, FormLabel,
    useToast, Input,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    useDisclosure
} from "@chakra-ui/react";
import { history } from "umi";

import { AutoLayout } from 'zero-element-boot/lib/components';

import promiseAjax from 'zero-element-boot/lib/components/utils/request';
import { bindingConvert } from 'zero-element-boot/lib/components/gateway/Binding'
import doFilter from 'zero-element-boot/lib/components/gateway/doFilter.mjs';

import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';

import AddNewBtn from '@/components/Presenter/AddNewButton';

//
import menuLayoutJson from './menuLayout';

//

const routeMap = {
    presenter: '/presenters',
    cart: '/carts',
    layout: '/layouts',
    container: '/containers',
}

const converter = {
    api:'api',
    layoutApi: 'layoutApi',
    layoutName: 'layoutName',
}

export default function Index(props) {

    const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 

    const { isOpen, onOpen, onClose } = useDisclosure()
    const [ layoutConfig, setLayoutConfig ] = useState({})
    const [ nextComponent, setNextComponent ] = useState('')
    const [ currentNavItem, setCurrentNavItem ] = useState('')
    const [ currentDrawerItem, setCurrentDrawerItem ] = useState('')
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

    function clickBtnToComponent(name) {
        const path = routeMap[name]
        history.push({
            pathname: path,
            query: {
                id,
                status:'edit'
            }
        })
    }

    const menuItemClick = (item) => {
        // setMenuName(item.name)
        
        if(item.isSelected){
            const convertData = bindingConvert(converter, item)
            const filterData = doFilter(converter, convertData)
            setCurrentNavItem(filterData)
            onOpen(true)
        }
    }

    const onDrawerItemClick = (item) => {
        if(item.isSelected){
            setCurrentDrawerItem(item)
        }
    }

    const onDrawerOk = () => {
        handleChangeData()
    }


    //当所有现有节点已完成动画输出时触发
    const loadedDrawerData = () => {

        //TODO
    }

    const config = {
        items: [{id:1}],
        layout: layoutConfig,
    }

    return (
        <ChakraProvider>
            <VStack align='stretch' spacing='-2'>
                <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                    <FormControl display='flex' alignItems='center'>
                        <Button colorScheme='teal' size='sm' marginRight={'10px'} onClick={() => history.push('/')}>
                            返回
                        </Button>
                        <AutoLayout 
                            layout={menuLayoutJson} 
                            onItemClick={menuItemClick}
                            // containerHeight={menuLayoutHeight} 
                        />
                    </FormControl>
                </Box>

                <Box style={{ margin: '10px 5px 10px 5px', paddingLeft: '8px' }} >
                
                {/* <AutoLayout {...config}/> */}
                
                {/* {
                    isLoading
                    ? <Spinner/>
                    : layoutConfig && layoutConfig.presenter && <AutoLayout {...config}/>
                } */}
                    
                </Box>

                {/* <Box style={{ margin: '10px 5px 10px 5px', paddingLeft: '8px', width: '40px' }} >
                    <a onClick={() => addComponent()}>
                        <AddNewBtn/>
                    </a>
                </Box> */}

            </VStack>


            <Drawer
                size='xl'
                isOpen={isOpen}
                placement='right'
                onClose={onClose}
                closeOnOverlayClick={false}
                onCloseComplete={loadedDrawerData}
            >
                <DrawerOverlay />
                <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>切换组件</DrawerHeader>

                <DrawerBody>
                    <PreviewAutoLayout {...currentNavItem} moduleId={moduleId} onItemClick={onDrawerItemClick}  />
                </DrawerBody>

                <DrawerFooter>
                    <Button variant='outline' mr={3} onClick={onClose}>
                    取消
                    </Button>
                    <Button colorScheme='blue' onClick={onDrawerOk}>确定</Button>
                </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </ChakraProvider>
    )
}