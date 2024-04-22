
const propsManageLayout = {
  children: [
      {
          xname: 'PreviewAutoLayout',
          props: {
              layoutName:"PropertyManage"
          }
      },
      {
          xname: 'PreviewAutoLayout',
          props: {
              layoutName:"PropKeyValueManage",
          }
      },
  ],
  xname:'HStack',
  props:{
    flexFlow: "no-wrap",
    spacing: 8
  },
  container: "DataFlowContainer"
}

const propsManageConverter = {
  assembledAs:"assembledAs"
}


const bindingManageLayout = {
  children: [
      {
          xname: 'PreviewAutoLayout',
          props: {
              layoutName:"BindingManageList"
          }
      },
      {
          xname: 'PreviewAutoLayout',
          props: {
              layoutName:"ParentParameterListAutoLayout",
          }
      },
  ],
  xname:'HStack',
  props:{
    flexFlow: "no-wrap",
    spacing: 8
  },
  container: "DataFlowContainer"
}

const bindingManageConverter = {
}

export {
  propsManageLayout,
  propsManageConverter,
  bindingManageLayout,
  bindingManageConverter,
}
  