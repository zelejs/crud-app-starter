import React, { useState, useEffect } from 'react';
import { Box, HStack, InputGroup, Input, InputRightElement, Button, Text, useToast } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
import menuLayoutJson from './menuLayout';
import childMenuLayout from './childMenuLayout';

const componentListIdmap = {
  presenter: 160,
  cart: 129,
  indicator: 161,
  selector: 163,
  container: 130,
  layout: 153
}

export default function EditComponent(props) {

  const { componentId, moduleName } = props;

  const originApi = '/openapi/lc/module?pageNum=1&pageSize=100'
  const originLayoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/'

  const [api, setApi] = useState()
  const [layoutApi, setLayoutApi] = useState()
  const [ menuName, setMenuName ] = useState('')
  const [ childMenuId, setChildMenuId ] = useState(0)
  const [ componentData, setComponentData ] = useState('')
  const [ currentModuleName, setCurrentModuleName ] = useState(moduleName)
  const toast = useToast()

  useEffect(_ => {
    // const count = resetData + 1;
    // setResetData(count)
  }, [])

  useEffect(_ => {
    if(menuName){
      setComponentData('card');
      // getComponentDDetail(menuName)
      getComponentList(menuName)
      const newApi = originApi + '&componentOption='+menuName
      const newLayoutApi = originLayoutApi + '/'+ componentListIdmap[menuName]
      setApi(newApi)
      setLayoutApi(newLayoutApi)
    }
  }, [menuName])

  //根据ID获取组件信息
  const getComponentDDetail = (componentName) => {
    const api = '/api/' + componentName
    const query = {
    }
    return promiseAjax(api, query).then(resp => {
        if (resp && resp.code === 200) {
          // TODO 存入组件信息
        } else {
            console.error('获取组件信息失败 ==', resp)
        }
    }).finally(_ => {
    });
  }

  //根据ID获取组件信息
  const getComponentList = () => {
    const query = {
    }
    return promiseAjax(api, query).then(resp => {
        if (resp && resp.code === 200) {
          // TODO 存入组件信息
        } else {
            console.error('获取组件信息失败 ==', resp)
        }
    }).finally(_ => {
    });
  }

  //修改moduleName
  const editModultName = () => {
    if(!currentModuleName){
      toastTips('请输入名称')
      return
    }
    const api = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/' + componentId
    const query = {
      moduleName: currentModuleName
    }
    return promiseAjax(api, query).then(resp => {
        if (resp && resp.code === 200) {
          toastTips('修改成功')
        } else {
            toastTips('修改失败')
        }
    }).finally(_ => {
    });

  }


  const menuItemClick = (item) => {
    console.log('menuItem = ', item)
    
    setApi('')
    setLayoutApi('')
    setMenuName(item.name)
  }

  const childMenuClick = (item) => {
    console.log('childMenu = ', item)
    // setChildMenuId(item)
  }

  //选择组件
  const onSelectComponentItemClick = (item) => {
    console.log('onSelectComponentItemClick = ', item)
  }

  const handleChangeInputValue = (e) => {
    setCurrentModuleName(e.target.value)
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
    <HStack spacing={'0'}>
      <Box style={{ width: '185px', height: '100vh', padding: '10px 20px', background: '#fff', display: 'flex', 
        alignItems: 'flex-start', justifyContent: 'center', flexDirection: 'column' }}>
          <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改名称</Text>
          <InputGroup size='md' marginBottom={8}>
            <Input placeholder='名称' defaultValue={currentModuleName} onChange={handleChangeInputValue} />
            <InputRightElement width='4.5rem'>
              <Button h='1.75rem' size='sm' onClick={() =>editModultName()}>
                确定
              </Button>
            </InputRightElement>
          </InputGroup>
          
        <Text fontSize={'14px'} fontWeight={'bold'} marginBottom={1}>修改组件</Text>
        <AutoLayout layout={menuLayoutJson} isScroll={false} onItemClick={menuItemClick} />
      </Box>
      <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>
      {
        componentData && (
          <Box display={'flex'} flex={1} flexDirection={'row'}>
            <Box style={{ width: '200px', height: '100vh', padding: '10px 20px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <AutoLayout layout={childMenuLayout} items={[]} onItemClick={childMenuClick} />
            </Box>
            <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>
            <Box flex={1} style={{ height: '100vh', padding: '10px 20px', background: '#fff' }}>
              { api && layoutApi && (
               <PreviewAutoLayout  layoutApi={layoutApi} api={api} onItemClick={onSelectComponentItemClick} />
              )}
            </Box>
          </Box>
        )
      }
      
    </HStack>
  )
}