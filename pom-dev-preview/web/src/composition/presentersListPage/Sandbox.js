import React, { useState, useEffect } from 'react';
import { Spinner, Box, VStack, FormControl, Button, useToast } from '@chakra-ui/react';
import { history } from 'umi'; 
import AutoLayout from './index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

const routeMap = {
    presenter: '/presenters',
    cart: '/carts',
    layout: '/layouts',
    container: '/containers',
}
export default function Index(props) {

    const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
    const toast = useToast()
    const [items, setItems] = useState('');
    const [isLoading, setLoading] = useState(false);

    useEffect(_ => {
        getData()
    },[])

    function getData() {
        setLoading(true)
        setItems([])
        let api = '/openapi/lc/module?componentOption=presenter&pageNum=1&pageSize=100'
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            setLoading(false)
            if (resp && resp.code === 200) {
                setItems(resp.data.records)
            } else {
                console.error("获取presenter数据失败")
            }
        });
    }

    //保存数据
    function saveData(itemData) {
        let api = '/openapi/lc/module/build-auto-layout/' + id
        const queryData = {
            addModuleId: itemData.id
        };
        promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
            if (resp && resp.code === 200) {
                toPage(resp.data.nextComponent)
            } else {
                console.error("添加presenter失败 = ", resp)
                toastTips(resp.message)
            }
        }).finally(_=>{
            setLoading(false)
        });
    }

    const itemClick = (itemData) => {
        // console.log('itemData == ', itemData)
        if(itemData.isSelected){
            saveData(itemData)
        }
    }

    const toPage = (nextComponent) => {
        const path = routeMap[nextComponent]
        history.push({
            pathname: path,
            query: {
                id
            }
        })
    }

    //返回详情页
    function goViewPage(){
        history.push({
            pathname:'/view',
            query:{
                id
            }
        })
    }

    // tips
    function toastTips(text, status = 'success') {
        toast({
            title: text,
            description: "",
            status: status,
            duration: 3000,
            isClosable: true,
            position: 'top'
        })
    }

    return (
        
        <VStack align='stretch' spacing='-2'>
            <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                <FormControl display='flex' alignItems='center'>
                    <Button colorScheme='teal' size='sm' onClick={() => goViewPage()}>
                        返回
                    </Button>
                </FormControl>
            </Box>

            <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
                {
                    isLoading
                    ? <Spinner/>
                    : items && items.length > 0 && <AutoLayout items={items} onPresenterItemClick={itemClick}  />
                }
            </Box>

        </VStack>
        
        
    )
}