module.exports = {
  xname: 'Flexbox',
  props: {
    direction: 'column', 
  },
  cart: {
    xname: 'Cart',
    props: {
      // padding: '16px',
      margin: '0',
      corner: 0,
      linewidth: '0',
      padding: '10px 25px',
      isOnHover:false
    }
  },
  container: 'PlainList',
  presenter:{
    xname: 'Flexbox',
    props: {
      align: 'start',
      direction: 'column',
      flexWidth: 'auto-full'
    },
    children: [
      {
        presenter: 'SwaggerItem',
        indicator:{
          xname:'ClickIndicator',
          binding: {
            id:"id",
            api:"api",
          }
        },
      },
    ]
  }
}