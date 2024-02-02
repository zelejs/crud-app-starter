import React, { useState, useEffect } from 'react';
import { Spinner, Box, VStack, Button } from '@chakra-ui/react';
import { history } from 'umi';
import AutoLayout from './index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


export default function Index(props) {

    const { id } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
    const [items, setItems] = useState('');
    const [isLoading, setLoading] = useState(false);

    useEffect(_ => {
        getData()
    },[])

    function getData() {
        
        setItems([])
        let api = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/'+id
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            setLoading(false)
            if (resp && resp.code === 200) {
                setItems(resp.data.lowAutoModuleProps || [])
            } else {
                console.error("获取属性数据失败")
            }
        });
    }

    //返回详情页
    function goViewPage(){
        history.push({
            pathname:'/',
            query:{
            }
        })
    }

    function toEditPage (data) {
        console.log('data = ', data)
        history.push({
            pathname:'/choose-attribute',
            query:{
                attrId: data.id,
                moduleId: data.moduleId
            }
        })
    }
    
    return (
        <VStack align='stretch' spacing='-2'>
            <Box style={{ margin: '5px 10px 15px 5px', paddingLeft: '8px' }}>
                <Button colorScheme='teal' size='sm' marginRight={'8px'} onClick={() => goViewPage()}>
                    返回
                </Button>
            </Box>

            <Box w={400}>
                {
                    isLoading
                    ? <Spinner/>
                    : items && items.length > 0 && <AutoLayout items={items} onAttrbuteItem={toEditPage}  />
                }
            </Box>

        </VStack>
        
    )
}