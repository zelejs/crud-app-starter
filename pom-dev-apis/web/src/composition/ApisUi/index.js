import React, { useState } from 'react';
import { 
    ChakraProvider, Flex, Center, Box, Stack, Spacer, VStack, Container, Button,
    InputGroup, Input, InputRightElement
} from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot/lib/components';
import Loading from 'zero-element-boot/lib/components/loading';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import layout from './layout';
import JsonTree from '@/composition/JsonTree/Sandbox'

export default function Index(props) {

    const { data=[], method='' } = props;

    const [ listData, setListData ] = useState(data)
    const [ isShowList, setIsShowList ] = useState(true);
    const [ isShowData, setIsShowData ] = useState(false);
    const [ isLoading, setIsLoading ] = useState(false);
    const [ showApiDetail, setApi ] = useState('');
    const [ searchValue, setSearchValue ] = useState('');
    const [ isShowBackBtn, setIsShowBackBtn ] = useState(false);
    const [ currentItemApi, setCurrentItemApi ] = useState('');
    const [ currentItemOldApi, setCurrentItemOldApi ] = useState('');
    const [ isShowCurrentItemApiStatus, setIsShowCurrentItemApiStatus ] = useState(false);
    

    let layoutData = '';
    const layoutJsonPath = '';
    const localLayoutJson = layout;

    if(layoutJsonPath){
        layoutData = { path: layoutJsonPath};
    }else{
        layoutData = localLayoutJson;
    }

    const config = {
        items: listData.length > 0 ? listData : [],
        layout: layoutData
    };

    const onApiItemClick = (item) => {
        // document.body.scrollTop = document.documentElement.scrollTop = 0;
        
        // console.log('item == ', item)
        const apiStr = `/openapi/crud/lc_low_auto_apis/lowAutoApis/lowAutoApises/${item.id}`
        setCurrentItemApi(item.api)
        setCurrentItemOldApi(apiStr)
        setApi(apiStr)
        setIsShowData(true)
        setIsShowList(false)
        setIsShowBackBtn(true)
    }

    //返回首页
    function goHome () {
        setIsShowList(true)
        setIsShowData(false)
        setSearchValue('')
        searchApiList('')
        setIsShowBackBtn(false)
    }

    function goBack () {
        if(isShowCurrentItemApiStatus){
            setApi(currentItemOldApi)
            setIsShowList(false)
            setIsShowData(true)
            setIsShowCurrentItemApiStatus(false)
        }else{
            setIsShowList(true)
            setIsShowData(false)
        }
    }

    //提交搜索栏信息
    const handleSearchClick = (e) => {
        searchApiList(searchValue)
    }

    //保存搜索栏信息
    const handleSearchValue = (e) => {
        setSearchValue(e.target.value)
    }

    //搜索
    function searchApiList(searchValue) {
        //通过apiName获取API路径
        const api = `/openapi/crud/lc_low_auto_apis/lowAutoApis/lowAutoApises`;
        const queryData = {
            pageNum: 1,
            pageSize: 1000,
            apiMethod: method,
            search: searchValue
        };
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setIsShowBackBtn(resp.data.records.length > 0 && true)
                setListData([{items: resp.data.records}])
            } else {
                console.error("获取 api 列表失败")
            }
        }).finally(_ => {
        });
    }

    //查看API
    function getApiDetail(){
        setApi(currentItemApi)
        setIsShowCurrentItemApiStatus(true)
        setIsShowData(true)
        setIsShowList(false)
    }

    return (
        <ChakraProvider>
            <Flex>
                
                <Box>
                    <VStack spacing='3px'>
                        <div style={{minWidth: '500px', width: '100%', height: '60px', lineHeight: '60px', backgroundColor: '#ffffff', padding:'20px 10px 10px 25px'}}>
                            <Stack direction={['row']} w="500px" h='40px' spacing='10px' align='center'>
                                <div style={{widht: '100px', height:'40px'}}>
                                    <Center>
                                        <Button w='100px' h="40px" colorScheme='blue' onClick={() => goHome()}>Home</Button>
                                    </Center>
                                </div>
                                {
                                    isShowList && (
                                        <InputGroup size='md'>
                                            <Input
                                                pr='4.5rem'
                                                type='text'
                                                value={searchValue}
                                                placeholder='Please Enter'
                                                onChange={handleSearchValue}
                                            />
                                            <InputRightElement width='4.5rem'>
                                                <Button h='1.75rem' size='sm' onClick={handleSearchClick}>
                                                    Search
                                                </Button>
                                            </InputRightElement>
                                        </InputGroup>
                                    )
                                }
                                {
                                    !isShowList && isShowBackBtn && listData.length > 0 && <Button w='100px' h="40px" colorScheme='blue' onClick={() => goBack()}>Back</Button>
                                }
                                {
                                    isShowData && showApiDetail && (
                                        <Button w='100px' h="40px" colorScheme='blue' onClick={() => getApiDetail()}>查看API</Button>
                                    )
                                }
                            </Stack>
                        </div>
                        
                        {
                            isShowList ? (
                                <AutoLayout {...config} onItemClick={onApiItemClick}>
                                </AutoLayout>
                            ): <></>
                        }
                        
                        {
                            isLoading ? (
                                    <Loading styles={{marginTop: '60px'}}/>
                            )
                             : isShowData && showApiDetail ? (
                                <div style={{width: '100%', paddingLeft:'25px'}}>
                                    <Box flex='1'>
                                        <div style={{background:'#ffffff', width:'100%', paddingTop: '15px'}}>
                                            <JsonTree api={showApiDetail}/>
                                        </div>
                                    </Box>
                                </div>
                            ): <></>
                        }
                    </VStack>
                </Box>
            </Flex>
        </ChakraProvider>
    )
}