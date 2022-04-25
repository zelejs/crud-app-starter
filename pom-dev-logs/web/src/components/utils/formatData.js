

module.exports = function formatData(props) {
  const { children } = props;

  return children.map(child => {
    const { presenter, field } = child;
    const chartName = presenter.replace(/^\S/, s => s.toUpperCase());
    const data = props[field] || {};

    if (!props[field]) {
      console.warn(`未能读取到数据 field: ${field}`, props);
    }

    return {
      ...child,
      presenter: chartName,
      data: data,
    }
  })
}