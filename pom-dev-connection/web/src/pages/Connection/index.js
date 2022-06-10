import React, { useState } from 'react';
import {
  ChakraProvider, Flex, Center, Box, Stack, Spacer, VStack, Container, Button, Input, Select, Tabs, TabList, TabPanels, Tab, TabPanel, FormControl, FormLabel, Switch, Spinner, Popover,
  PopoverTrigger,
  PopoverContent,
  PopoverHeader,
  PopoverBody,
  PopoverFooter,
  PopoverArrow,
  PopoverCloseButton,
  PopoverAnchor,
  Portal, Grid, HStack,
  sm, closeOnEsc, Textarea, Tooltip

} from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
// import AutoLayout from '../AutoLayout';
import Loading from 'zero-element-boot/lib/components/loading';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');
// import config from 'zero-element-boot\lib\components\config'
import layout from './layout';

import { setEndpoint, setToken, getEndpoint } from 'zero-element-boot/lib/components/config/common';

export default function Index (props) {

  if (process.env.NODE_ENV == 'development') {
    setEndpoint('http://demo.smallsaas.cn:8001');
  }

  const { data = [] } = props;

  const [isShowList, setIsShowList] = useState(true);
  const [isShowData, setIsShowData] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [showDetail, setDetail] = useState('');
  const [currentItemName, setCurrentItemName] = useState('');
  const [switchStatus, setSwitchStatus] = useState(true)
  const [showsetcontent, setmycontent] = useState('')
  const [showybutton, setmybutton] = useState(false)
  const [showRulercontent, setRulercontent] = useState('');
  const [showmySign, setmySign] = useState('')
  const [showRulertitle, setRulercode] = useState('')
  const [showtitle, settitle] = useState('')



  // const [showSign,setSigncntent] = serState('')



  let layoutData = '';
  const layoutJsonPath = '';
  const localLayoutJson = layout;



  // 用于下载
  // if (process.env.NODE_ENV === 'development') {
  //   URL = `http://demo.smallsaas.cn:8001`;
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

  // //
  // const getDetailFetch = async (name, num) => {
  //   if (num == 1) {
  //     setCurrentItemName(name)
  //   }
  //   // const api = `http://localhost:8080/api/dev/dependency/decompile`;
  //   setIsShowList(false)
  //   setIsLoading(true)
  //   promiseAjax(api, { pattern: name }, {})
  //     .then(responseData => {
  //       if (responseData && responseData.code === 200) {
  //         let respData = responseData.data;
  //         setDetail(respData);
  //         setIsShowData(true)

  //       } else {
  //         setIsShowList(true)
  //         setIsShowData(false)
  //       }
  //       setIsLoading(false)
  //     })

  // }
  // console.log(statenum);

  //输入sign框 点击数据表 按钮 获取数据表
  function signWay () {
    getconnection(signdata)

    //签名文本框定时获取焦点
    SignText()

  }
  //全局添加sign签名
  var signdata = ''
  const signcontent = (m) => {

    signdata = m
    setmySign(signdata)
    console.log();
  }


  //获取所有表
  function getconnection () {
    let api = `/dev/connection?sign=${showmySign}`;

    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code
          === 200) {
          let respData = responseData.data;
          setDetail(respData);
          setIsShowData(true)
          setmybutton(false)
          // console.log(respData);
        } else {
          setIsShowList(true)
          setIsShowData(false)
          alert('签名已过期!')
        }
        setIsLoading(false)

      })


  }
  //获取数据库快照

  function getBaseSt () {
    getBaseStWay()

  }

  function getBaseStWay () {
    let api = `/dev/connection/snapshot?sign=${showmySign}`;

    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          setDetail(respData);
          setIsShowData(true)
          setmybutton(false)
          // console.log(respData);
        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })

  }


  //数据库下载--
  function downFiles (content) {
    downFileanniu(content)

  }

  function downFileanniu (content) {
    let api = `/dev/connection/snapshot/instant?sign=${showmySign}&ruler=${content}`
    const w = window.open('about:blank');
    w.location.href = api
    console.log(api);
    setSwitchStatus(false)
  }



  //获取规则
  function signgetRulerWay () {
    getRuler()
    //签名文本框定时获取焦点
    SignText()
  }

  function getRuler () {

    let api = `/dev/connection/snapshot/rulers?sign=${showmySign}`;

    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          setDetail(respData);
          setIsShowData(true)
          setmybutton(true)

          // console.log(respData);
          // setRulertitle(respData)
        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })

  }



  //新建规则
  function newRuler () {
    newRulerWay(ruler, rulercontent)

  }

  //文本信息
  var ruler = ''
  const setrulers = (n) => {
    ruler = n
  }
  var rulercontent = ''
  const setcondatas = (m) => {
    rulercontent = m
  }

  //搜索按钮--获取返回的数据 //新建、更新方法
  function newRulerWay (ruler, rulercontent) {
    let api = `/dev/connection/snapshot/rulers/${ruler}?sign=${showmySign}`;

    //字符串转化成JSON
    // console.log(api);
    let rulerdata = JSON.parse(rulercontent)
    promiseAjax(api, rulerdata, { method: 'POST' })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respdata = responseData.data;
            // console.log(respdata);
            setDetail(respdata);
            setIsShowData(true)
            setSwitchStatus(false)


            alert('新建成功！')

          } else {
            setIsShowList(true)

            setIsShowData(false)
          }
          setIsLoading(false)
        }
      })


  }





  //更新规则

  function updataName () {
    updataRulerWay(showtitle, condata)

  }

  //文本信息
  var content = ''
  const setcontent = (n) => {
    settitle(n)
    console.log(settitle);

  }
  var condata = ''
  const setcondata = (m) => {
    condata = m
  }

  //搜索按钮--获取返回的数据 更新方法
  function updataRulerWay (content, condata) {
    let api = `/dev/connection/snapshot/rulers/${content}?sign=${showmySign}`;

    //字符串转化成JSON

    let rulerdata = JSON.parse(condata)
    promiseAjax(api, rulerdata, { method: 'POST' })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respdata = responseData.data;

            setDetail(respdata);
            setIsShowData(true)
            setSwitchStatus(false)

            setRulercontent(respdata)
            console.log(respdata);
            alert('更新成功！')

          } else {
            setIsShowList(true)
            setIsShowData(false)

          }
          setIsLoading(false)
        }
      })


  }

  //


  //根据规则(ruler)保存数据库快照到服务器本地
  function Localrule (content) {
    LocalruleStorage(content)
  }


  //方法
  function LocalruleStorage (content) {
    let api = `/dev/connection/snapshot?sign=${showmySign}&ruler=${content}`;
    console.log(api);
    promiseAjax(api, {}, { method: 'POST' })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respData = responseData.data;
            // console.log(respData);
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






  //根据规则(ruler)页面输出当前的数据库快照内容
  function printData (content) {
    printDataWay(content)

  }

  //方法
  function printDataWay (content) {
    let api = `/dev/connection/snapshot/print/json?sign=${showmySign}&ruler=${content}`;
    console.log('api：' + api);
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          console.log(respData);
          setDetail(respData);
          setIsShowData(true)

        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })



  }

  // 查看具体的命名规则的配置详情
  function RulerDeploy (content) {
    RulerDeployWay(content)

  }

  //查看具体的命名规则的配置详情//方法
  function RulerDeployWay (content) {

    let api = `/dev/connection/snapshot/rulers/json/${content}?sign=${showmySign}`
    console.log(api);

    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          // respData = respData.replace(/[\'\\\\/\b\f\n\r\t]/g, '');
          // let respDataJSON = JSON.stringify(respData)
          // setDetail(respData);
          // let respData = jqdata.toString()
          // let reg = /[,，]/g;

          // respData = respData.replace(reg, "$&\r\n");
          setDetail(respData)

          console.log(respData);
          setIsShowData(true)

        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })

  }



  //根据规则名删除规则
  function deleteRulerData (content) {
    deleteDatatWay(content)

  }

  function deleteDatatWay (content) {
    let api = `/dev/connection/snapshot/rulers/${content}?sign=${showmySign}`;
    console.log(api);
    promiseAjax(api, {}, { method: 'DELETE' })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respData = responseData.data;
            // console.log(respData);
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

  function downSnapshot (content) {
    // console.log(content);
    printdataBaseSWay(content)
    // console.log(signdata);
    getBaseSt()
  }

  // //下载snapshot文件
  function printdataBaseSWay (content) {
    let api = `/dev/connection/snapshot/dl?sign=${showmySign}&pattern=${content}`

    const w = window.open('about:blank');
    const host = getEndpoint() || location.host
    w.location.href = host + api
    console.log(host + api);
  }



  //删除保存在本地的快照文件
  function deleteBase (content) {
    deleteBaseWay(content)

  }
  function deleteBaseWay (content) {
    let api = `/dev/connection/snapshot/${content}?sign=${showmySign}`
    console.log(api);

    promiseAjax(api, {}, { method: 'DELETE' })
      .then(responseData => {
        {
          if (responseData && responseData.code === 200) {
            let respData = responseData.data;
            // console.log(respData);
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






  //获取images接口文件列表
  function getImages (content) {
    getImagesWay(content)
    //签名文本框定时获取焦点
    SignText()
  }

  function getImagesWay (content) {
    let api = `/dev/connection/images?sign=${showmySign}`;
    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          setDetail(respData);
          setIsShowData(true)
          setmybutton(false)

        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })




  }

  //获取

  //获取images接口文件列表
  function getshow (content) {
    getshowWay(content)
    //签名文本框定时获取焦点
    SignText()
  }

  function getshowWay (item) {

    let api = `/dev/connection/json?sign=${showmySign}&pattern=${item}`;
    // let api = `/dev/connection/schema/json?pattern=${item}`;
    setIsShowList(false)
    setIsLoading(true)
    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let respData = responseData.data;
          let resData = JSON.stringify(respData);
          let resDataJS = JSON.parse(resData)
          setDetail(resDataJS);
          setIsShowData(true)

        } else {
          setIsShowList(true)
          setIsShowData(false)
        }
        setIsLoading(false)
      })

  }


  //更新规则
  function getRulerContent (content) {

    settitle(content)
    getRulerContentWay(content)
    //签名文本框定时获取焦点

  }
  function getRulerContentWay (name) {
    let api = `/dev/connection/snapshot/rulers/json/${name}?sign=${showmySign}`

    promiseAjax(api)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          let resData = responseData.data;
          setRulercontent(resData)

          console.log(resData);

        }
      })

  }
  //签名文本框定时获取焦点
  function SignText () {
    // setTimeout(() => {
    //   document.getElementById('mysignText').focus()
    // }, 0);
  }





  //处理返回内容
  function handleContent (data) {


    if (typeof data === 'string') {
      return data;
    }
    if (data instanceof Array && data.length > 0) {
      return (
        <Stack spacing='3px'>
          {
            data.map((item, index) => {


              if (item.indexOf(".sql") > -1) {

                // onMouseMove={out()}
                return <div key={`${index}_item`} >




                  <div style={{ position: 'absolute', left: '20px', marginTop: '6px' }}>  <a href='#'>{item}</a></div>

                  <div style={{ position: 'relative', left: '150px', marginTop: '6px' }}>
                    <Box >
                      <Flex>
                        <div>
                          <Tooltip label='下载snapshot文件' placement='top'>
                            <Button size={'xs'} colorScheme={'blue'} left='10px' onClick={() => downSnapshot(item)}>下载</Button></Tooltip>
                        </div>
                        <div>

                          <Tooltip label='删除保存在本地的快照文件' placement='top'>   </Tooltip>
                          <Popover>
                            <PopoverTrigger>
                              <Button size={'xs'} colorScheme={'red'} left='20px'>删除</Button>
                            </PopoverTrigger>
                            <Portal>
                              <PopoverContent>
                                <PopoverArrow />
                                <PopoverHeader>确定删除该快照文件吗？</PopoverHeader>
                                <PopoverCloseButton />
                                <PopoverBody>
                                  <Button onClick={() => deleteBase(item)}>确定
                                  </Button>
                                </PopoverBody>
                                <PopoverFooter></PopoverFooter>
                              </PopoverContent>
                            </Portal>
                          </Popover>

                        </div>

                      </Flex>

                    </Box>

                  </div>
                </div>;

              } else {
                if (item.indexOf("ruler") > -1) {


                  return (
                    <div key={`${index}_item`} >

                      <Tooltip label='查看该规则的配置详情' placement='top'>
                        <div style={{ position: 'absolute', left: '20px', marginTop: '6px' }}><a href="#" onClick={() => RulerDeploy(item)}>{item}</a></div>
                      </Tooltip>

                      <Box style={{ position: 'relative', left: '150px', marginTop: '6px' }}>
                        <Flex>
                          <Popover>
                            <PopoverTrigger>
                              {/* <Tooltip label='更新规则内容' placement='top'> */}
                              <Button size={'xs'} colorScheme={'blue'} left='6px' onClick={() => getRulerContent(item)}>更新规则</Button>
                              {/* </Tooltip> */}
                            </PopoverTrigger>
                            <Portal>
                              <PopoverContent left={'500px'}>
                                <PopoverArrow />
                                <PopoverHeader>更新规则内容</PopoverHeader>
                                <PopoverCloseButton />
                                <PopoverBody>
                                  <Input placeholder='规则名' value={showtitle} onChange={(N) => setcontent(N.target.value)} />
                                  <Textarea marginTop={'10px'} height={'200px'} onMouseOut={(N) => setcondata(N.target.value)} defaultValue={showRulercontent}></Textarea>
                                  <Button colorScheme={'blue'} marginTop={'10px'} left='120px' onClick={() => updataName()}>保存</Button>
                                </PopoverBody>
                              </PopoverContent>
                            </Portal>
                          </Popover>
                          <Tooltip label='下载数据库快照' placement='top'>
                            <Button size={'xs'} colorScheme={'blue'} left='20px' onClick={() => downFiles(item)}>实时备份</Button>
                          </Tooltip>
                          <Tooltip label='数据库执行保存到规则快照' placement='top'>
                            <Button size={'xs'} colorScheme={'blue'} left='30px' onClick={() => Localrule(item)}>执行</Button>
                          </Tooltip>
                          <Tooltip label='页面输出该规则的数据库快照' placement='top'>
                            <Button size={'xs'} colorScheme={'blue'} left='40px' onClick={() => printData(item)}>输出</Button>
                          </Tooltip>


                          <Popover>
                            <PopoverTrigger>
                              <Button size={'xs'} colorScheme={'red'} left='50px'>删除</Button>
                            </PopoverTrigger>
                            <Portal>
                              <PopoverContent>
                                <PopoverArrow />
                                <PopoverHeader>确定删除该规则吗？</PopoverHeader>
                                <PopoverCloseButton />
                                <PopoverBody>
                                  <Button onClick={() => deleteRulerData(item)}>确定</Button>
                                </PopoverBody>
                                <PopoverFooter></PopoverFooter>
                              </PopoverContent>
                            </Portal>
                          </Popover>

                        </Flex>

                      </Box>
                    </div>
                  )
                } else {
                  return <Container onClick={() => getshow(item)} maxW='container.xl' key={`${index}_item`}><a href='#'>{item}</a>  </Container>
                }
              }
            })
          }
        </Stack >
      )
    }

  }



  return (
    <ChakraProvider>
      <Flex>
        <Box>
          <VStack spacing='3px'>
            <div style={{ minWidth: '800px', width: '100%', height: '20px', lineHeight: '60px', backgroundColor: '#ffffff', padding: '20px 10px 10px 25px' }}>
              <Stack direction={['column', 'row']} w="100%" spacing='10px'>
                {/* <Button h="35px" colorScheme='blue' onClick={() => goBack()}>首页</Button> */}
                {currentItemName ? (
                  <Button h="35px" colorScheme='blue' onClick={() => getDetailFetch(currentItemName, 1)}>{currentItemName}</Button>
                ) : <></>}
              </Stack>
            </div>



            <div> <Tabs variant='soft-rounded' colorScheme='green' >
              <TabList style={{ position: 'absolute', left: '32% ' }}>
                <Tab onClick={() => signWay()}>数据表</Tab>
                <Tab onClick={() => signgetRulerWay()}>规则配置</Tab>
                <Tab onClick={() => getBaseSt()}>规则快照</Tab>
                <Tab onClick={() => getImages()}>images</Tab>
              </TabList>
            </Tabs>

              <Input id='mysignText' placeholder='请输入签名' style={{ position: 'absolute', left: '210px', top: '80px', width: '360px', padding: '15px' }} onBlur={(N) => signcontent(N.target.value)} />

              {/* //新建规则 */}
            </div>
            {showybutton ? (<div style={{ position: 'absolute', left: '30px', top: '70px', }} >
              <Popover>
                <PopoverTrigger >
                  <Button colorScheme={'blue'} left='6px'>新建规则</Button>
                </PopoverTrigger>
                <Portal>
                  <PopoverContent left={'500px'}>
                    <PopoverArrow />
                    <PopoverHeader>新建规则</PopoverHeader>
                    <PopoverCloseButton />
                    <PopoverBody>
                      {/* <Textarea onMouseOut={(N) => setcontent(N.target.value)}>{item}</Textarea> */}
                      <Textarea onBlur={(N) => setrulers(N.target.value)} />

                      <Textarea placeholder='请输入内容' height={'200px'} marginTop={'5px'} onBlur={(M) => setcondatas(M.target.value)} />
                      <Button colorScheme={'blue'} marginTop={'5px'} left='120px' onClick={() => newRuler()}>保存</Button>
                    </PopoverBody>
                  </PopoverContent>
                </Portal>
              </Popover>
            </div>
            ) : <></>}


            <div style={{ minWidth: '1200px', marginTop: '80px' }}>   {
              isShowList ? (
                <AutoLayout {...config} onItemClick={onJarItemClick}>
                </AutoLayout>
              ) : <></>
            }</div>

            {
              isLoading ? (
                <Loading styles={{ marginTop: '60px' }} />
              )
                : isShowData && showDetail ? (
                  <div style={{ width: '100%', paddingLeft: '50px' }}>
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

      {/* <div style={{ whiteSpace: 'pre-wrap' }} >{showsetcontent}</div> */}
    </ChakraProvider>



  )

}






