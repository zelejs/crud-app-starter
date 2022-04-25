import React, { useState } from 'react';
import { ChakraProvider, Flex, Center, Box, Stack, Spacer, VStack, Container, Button, Input, Select } from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
// import AutoLayout from '../AutoLayout';
import Loading from 'zero-element-boot/lib/components/loading';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import JarItem from './JarItem';

import layout from './layout';

// import StandaloneBody from './StandaloneBody';
// import { isDisabled } from '@chakra-ui/react/node_modules/@chakra-ui/utils';


export default function Index (props) {

  const { data = [] } = props;

  const [isShowList, setIsShowList] = useState(true);
  const [isShowData, setIsShowData] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [showDetail, setDetail] = useState('');
  const [currentItemName, setCurrentItemName] = useState('');
  //

  let layoutData = '';
  const layoutJsonPath = '';
  const localLayoutJson = layout;

  // let api = '/dev/dependency/decompile/json';
  let api = '';

  // if (process.env.NODE_ENV === 'development') {
  //   api = `http://192.168.3.121:8080${api}`;
  // }

  if (layoutJsonPath) {
    layoutData = { path: layoutJsonPath };
  } else {
    layoutData = localLayoutJson;
  }

  const config = {
    items: data.length > 0 ? data : [],
    layout: layoutData
  };

  const onJarItemClick = (item) => {
    console.log(item, ' === item')
    // document.body.scrollTop = document.documentElement.scrollTop = 0;
    let name = item.value;
    // if(name.indexOf('/') > -1){
    //     const list = name.split('/');
    //     name = list[list.length-1]
    // }

    if (name.indexOf('@') > -1) {
      const list = name.split('@');
      name = list[0]
    }
    setDetail([])
    getDetailFetch(name, 1)
  }

  //
  const getDetailFetch = async (name, num) => {
    if (num == 1) {
      setCurrentItemName(name)
    }
    // const api = `http://localhost:8080/api/dev/dependency/decompile`;
    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api, { pattern: name }, {})
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          setDetail(respData);
          setIsShowData(true)

        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })

  }
  // console.log(statenum);
  var searchData = ''
  // var upDown =''
  //搜索输入框
  const setSearchContent = async (e,) => {
    searchData = e
  }

  var upDown = ''
  const setupDown = (N) => {
    upDown = N
  }





  //搜索按钮--获取返回的数据
  function anniu (body) {
    let url = ''
    promiseAjax(url, { ...body })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respData = responseData.data;
            setDetail(respData);
            setIsShowData(true)
          } else {
            setIsShowList(true)
            setIsShowData(false)
          }
          setIsLoading(false)
        }
      })
  }
  //搜索方法
  function seach () {
    const body = {
      pattern: currentItemName,
      filter: searchData,
      N: upDown
    }
    anniu(body)

  }
  //select//获取value值
  // function getvalue () {
  //   var valuenum = document.getElementById('valueid').value;
  //   alert(valuenum)
  // }



  //
  //处理返回内容
  function handleContent (data) {

    if (typeof data === 'string') {
      return data;
    } if (data instanceof Array && data.length > 0) {
      return (
        <Stack spacing='3px'>
          {
            data.map((item, index) => {
              if (item.indexOf("/*") > -1 || item.indexOf("*") > -1) {
                return <div style={{ whiteSpace: 'pre-wrap' }} key={`${index}_item`}>{item}</div>;
              } else {
                // return  <Container maxW='container.xl' key={index}>{item}</Container>
                if (item.indexOf("BOOT-INF") > -1) {
                  return (
                    <div key={`${index}_item`} onClick={() => getDetailFetch(item, 2)}>
                      <JarItem value={item} />
                    </div>
                  )
                } else {
                  return <Container maxW='container.xl' key={`${index}_item`}>{item}</Container>
                }
              }
            })
          }
        </Stack>
      )
    } else {
      return '';
    }

  }


  function goBack () {
    setIsShowList(true)
    setIsShowData(false)
    setCurrentItemName('')

  }


  return (
    <ChakraProvider>
      <Flex>
        <Box>
          <VStack spacing='3px'>
            <div style={{ minWidth: '800px', width: '100%', height: '20px', lineHeight: '60px', backgroundColor: '#ffffff', padding: '20px 10px 10px 25px' }}>
              <Stack direction={['column', 'row']} w="100%" spacing='10px'>
                <Button h="35px" colorScheme='blue' onClick={() => goBack()}>Home</Button>
                {currentItemName ? (
                  <Button h="35px" colorScheme='blue' onClick={() => getDetailFetch(currentItemName, 1)}>{currentItemName}</Button>
                ) : <></>}
              </Stack>
            </div>

            <div style={{ minWidth: '800px', width: '100%', lineHeight: '60px', backgroundColor: '#ffffff', padding: '20px 10px 10px 25px' }}>

              <div style={{ left: '60%', width: '200px', top: '100px', height: '40px' }}>
                <Input placeholder='输入上下文数量' onChange={(N) => setupDown(N.target.value)} width='150px' />
              </div>

              <div style={{ position: 'absolute', left: '180px', top: '54px' }}>
                <Input placeholder='请输入您想要的日志内容' onChange={(e) => setSearchContent(e.target.value)} width='300px' /></div>

              <div style={{ position: 'absolute', left: '486px', top: '52px' }}>
                <Button colorScheme='teal' onClick={() => seach()} >搜索</Button>
              </div>

              {/* <Select placeholder='Select' onClick={getvalue()} >
                <option value='1'>1</option>
                <option value='2'>2</option>
                <option value='3'>3</option>
              </Select> */}
            </div>







            {/* <Input placeholder='请输入搜索内容' onChange={(e) => setSearchContent(SearchContent, 2)} /> */}



            {/* <Select placeholder='medium size' size='md' /> */}
            <div style={{ minWidth: '800px' }}>   {
              isShowList ? (
                <AutoLayout {...config} onItemClick={onJarItemClick}>
                  {/* <StandaloneBody  onItemClick={onJarItemClick}/> */}
                </AutoLayout>
              ) : <></>
            }</div>

            {
              isLoading ? (
                <Loading styles={{ marginTop: '60px' }} />
              )
                : isShowData && showDetail ? (
                  <div style={{ width: '100%', paddingLeft: '25px' }}>
                    <Box flex='1'>
                      {showDetail && showDetail.length > 0 ? (
                        <div style={{ background: '#ffffff', width: '100%', paddingTop: '10px' }}>
                          {handleContent(showDetail)}
                        </div>
                      ) : null}
                    </Box>
                  </div>
                ) : <></>
            }
          </VStack>

        </Box>

      </Flex>
    </ChakraProvider>
  )
}