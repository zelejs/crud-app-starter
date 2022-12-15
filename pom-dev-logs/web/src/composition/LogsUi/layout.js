module.exports = {
  xname: 'Flexbox',
  props: {
    align: 'start',
    direction: 'column',
    justify: 'half'
  },
  cart: {
    xname: 'Cart',
    props: {
      margin: 0,
      corner: 0,
      linewidth: '0',
      padding: '5px 25px',
      isOnHover: false
    },
  },
  indicator:{
    xname:'DownloadIndicator',
    props:{
      action: '/dev/logs/down/log?fileName=(fieldName)'
    },
    binding: {
      "value":"fieldName"
    }
  },
  container: 'PlainList',
  presenter: {
    xname: 'Flexbox',
    props: {
      justify: 'full'
    },
    children: [
      {
        xname: 'JarItem',
        indicator:{
          xname:'ClickIndicator',
          binding: {
            "value":"value"
          }
        },
      },
    ]
  }
}