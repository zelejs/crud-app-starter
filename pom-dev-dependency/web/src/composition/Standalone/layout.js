module.exports = {
  xname: 'Flexbox',
  props: {
    align: 'start',
    direction: 'column'
  },
  // gateway: {
  //   xname: 'Binding',
  //   props: {
  //     binding: {
  //       url: 'url',
  //       title: 'title',
  //       describe: 'describe',
  //       adType: 'adType',
  //       createTime: 'createTime'
  //     }
  //   }
  // },
  cart: {
    xname: 'Cart',
    props: {
      margin: '0',
      corner: 0,
      linewidth: '0',
      padding: '1px 25px',
      isOnHover:false
    }
  },
  container: 'PlainList',
  presenter:{
    xname: 'Flexbox',
    props: {
      direction: 'start', 
      justify: 'center row'
    },
    children: [
      {
        presenter: 'JarItem',
        indicator:{
          xname:'ClickIndicator',
          binding: {
            id:"id",
            value:"value",
          }
        },
      },
    ]
  }
}