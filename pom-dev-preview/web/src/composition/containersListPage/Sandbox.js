import React, { useState, useEffect } from 'react';
import { Spinner, Box, VStack, FormControl, Button, useToast  } from '@chakra-ui/react';
import { history } from 'umi'; 
import AutoLayout from './index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


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
        let api = '/openapi/lc/module?componentOption=container&pageNum=1&pageSize=100'
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setItems( resp.data.records)
            } else {
                console.error("获取container数据失败")
            }
        }).finally(_=>{
            setLoading(false)
        });
    }

     //保存数据
     function saveData(itemData) {
        let api = '/openapi/lc/module/build-auto-layout/'+id
        const queryData = {
            addModuleId:itemData.id,
        };
        promiseAjax(api, queryData, {method: 'PATCH'}).then(resp => {
            setLoading(false)
            if (resp && resp.code === 200) {
                goViewPage()
            } else {
                console.error("添加container失败 = ", resp)
                toastTips("添加失败")
            }
        });
    }
    

    //返回详情页
    function goViewPage(){
        history.push({
            pathname: '/view',
            query:{
                id
            }
        })
    }

    const itemClick = (itemData) => {
        // console.log('itemData == ', itemData)
        saveData(itemData)
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
                    <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
                        完成
                    </Button>
                </FormControl>
            </Box>

            <Box style={{ margin: '5px 5px 5px 5px', paddingLeft: '8px' }} >
                {
                    isLoading
                    ? <Spinner/>
                    : items && items.length > 0 && <AutoLayout items={items} onContainerItemClick={itemClick}  />
                }
            </Box>

        </VStack>
        
    )
}