import React, { useState } from "react";
import { ChakraProvider, Flex, Center, Box, Stack, Spacer, VStack, Container, Button } from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
import Loading from 'zero-element-boot/lib/components/loading';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import JarItem from './JarItem';

import layout from './layout';
import index from "..";
export default function Index (props) {
  const { data = [] } = props;

  const [isShowList, setIsShowList] = useState(true);
  const [isShowData, setIsShowData] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [showDetail, setDetail] = useState('');
  const [currentItemName, setCurrentItemName] = useState('');


  let layoutData = '';
  const layoutJsonPath = '';
  const localLayoutJson = layout;


  let api = '';


  if (layoutJsonPath) {
    layoutData = { path: layoutJsonPath }
  } else {
    layoutData = localLayoutJson;
  }

  const config = {
    items: data.length > 0 ? data : [],
    layout: layoutData
  };


  const onJarItemClick = (item) => {
    console.log(item, '===item');

    let name = item.value;
    if (name.indexOf('@') > -1) {
      const list = name.split('@');
      name = list[0]
    }

    setDetail([])
    getDetailFetch(name, 1)


  }
  const getDetailFetch = async (name, num) => {
    if (num == 1) {
      setCurrentItemName(name)
    }
    setIsShowList(false)
    setIsLoading(true)

    promiseAjax(api, { pattern: name }, {}).then(
      responseData => {
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
    )
  }
  // 
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






}
