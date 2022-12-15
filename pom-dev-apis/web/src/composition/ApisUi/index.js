import React, { useState } from 'react';
import { 
    Center, Box, Stack, VStack, Button,
    InputGroup, Input, InputRightElement, Select, Tooltip
} from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot/lib/components';
import Loading from 'zero-element-boot/lib/components/loading';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import layout from './layout';
import JsonTree from 'zero-element-boot/lib/components/presenter/tree/JsonTree/Sandbox'

import Pagination from 'rc-pagination';
require('rc-pagination/assets/index.css')

export default function Index(props) {

    const { data=[], method='', op, onDelAction, total=0, size=10 } = props;


    const [ listData, setListData ] = useState(data)
    const [ pageCurr, setPageCurr ] = useState(1)
    const [ pageTotal, setPageTotal ] = useState(total)
    const [ pageSize, setPageSize ] = useState(size)
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
        layoutData.indicator.props.isDisabled = op
    }

    const config = {
        items: listData.length > 0 ? listData : [],
        layout: layoutData
    };

    const onApiItemClick = (item) => {
        // document.body.scrollTop = document.documentElement.scrollTop = 0;
        
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
        setSearchValue('')
        setIsShowList(true)
        setIsShowData(false)
        setPageSize(10)
        searchApiList(true,1, 10)
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
        searchApiList(false, 1)
    }

    //保存搜索栏信息
    const handleSearchValue = (e) => {
        setSearchValue(e.target.value)
    }

    //搜索
    function searchApiList(refresh, pCurr=1, pSize) {
        setPageCurr(pCurr)
        setIsLoading(true)
        //通过apiName获取API路径
        const api = `/openapi/crud/lc_low_auto_apis/lowAutoApis/lowAutoApises`;
        const queryData = {
            pageNum: refresh ? 1 : pCurr,
            pageSize: pSize || pageSize,
            apiMethod: method,
            search: refresh ? '' : searchValue
        };
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                // setPageCurr(resp.data.current)
                setPageTotal(resp.data.total)
                setPageSize(resp.data.size)
                setIsShowBackBtn(resp.data.records.length > 0 && true)
                setListData(resp.data.records || [])
            } else {
                console.error("获取 api 列表失败")
            }
        }).finally(_ => {
            setIsLoading(false)
        });
    }

    //查看API
    function getApiDetail(){
        setApi(currentItemApi)
        setIsShowCurrentItemApiStatus(true)
        setIsShowData(true)
        setIsShowList(false)
    }

    //按回车触发搜索
    function onKeyDown(e){
        if(e.keyCode === 13){
            searchApiList(false, 1)
        }
    }

    //分页
    function changePage(e){
        searchApiList(false, e)
    }

    function showSizeChange(e){
        setPageSize(e.target.value)
        searchApiList(false, 1, e.target.value)
    }

    function SelectCompx () {
        return <Select h={'30px'} defaultValue={pageSize} onChange={showSizeChange}>
            <option value={10}>10条/页</option>
            <option value={20}>20条/页</option>
            <option value={50}>50条/页</option>
      </Select>
    }

    return (
        <VStack maxW='1300px' spacing='3px' align='left'>
            <div style={{minWidth: '500px', width: '100%', height: '60px', lineHeight: '60px', backgroundColor: '#ffffff', padding:'20px 10px 10px 25px'}}>
                <Stack direction={['row']} w="500px" h='40px' spacing='10px' align='center'>
                    {
                        isShowList && (
                            <InputGroup size='md'>
                                <Input
                                    pr='4.5rem'
                                    type='text'
                                    value={searchValue}
                                    placeholder='Please Enter'
                                    onChange={handleSearchValue}
                                    onKeyDown={onKeyDown}
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
                        !isShowList && isShowBackBtn && listData.length > 0 && <Button w='100px' h="35px" colorScheme='facebook' onClick={() => goBack()}>返回</Button>
                    }
                    {
                        isShowData && showApiDetail && (
                            <Button w='100px' h="35px" colorScheme='facebook' onClick={() => getApiDetail()}>查看API</Button>
                        )
                    }
                    
                    <Button w="35px" h="35px" borderRadius="4px" padding="9px" colorScheme='facebook' onClick={() => goHome()}>
                        <svg t="1670998813604" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2149" fill="#ffffff" width="128" height="128"><path d="M960 416V192l-73.056 73.056a447.712 447.712 0 0 0-373.6-201.088C265.92 63.968 65.312 264.544 65.312 512S265.92 960.032 513.344 960.032a448.064 448.064 0 0 0 415.232-279.488 38.368 38.368 0 1 0-71.136-28.896 371.36 371.36 0 0 1-344.096 231.584C308.32 883.232 142.112 717.024 142.112 512S308.32 140.768 513.344 140.768c132.448 0 251.936 70.08 318.016 179.84L736 416h224z" p-id="2150"></path></svg>
                    </Button>
                </Stack>
            </div>

            {
                isShowList ? (
                    <>
                        <div style={{ minHeight: '680px'}}>
                            {
                                isLoading ? (
                                    <Loading styles={{marginTop: '60px'}}/>
                                ):(
                                    <AutoLayout {...config} onItemClick={onApiItemClick} onItemDeleted={onDelAction}>
                                    </AutoLayout>
                                )
                            }
                        </div>
                        <Stack align='center'>
                            <Pagination total={pageTotal} pageSize={pageSize} current={pageCurr} 
                                onChange={changePage}
                                showSizeChanger
                                selectComponentClass={SelectCompx}
                            />
                        </Stack>
                    </>
                ):<></>
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
    )
}