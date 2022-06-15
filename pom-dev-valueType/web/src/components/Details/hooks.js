import { useModel } from 'zero-element/lib/Model';
import { useDidMount, useWillUnmount } from 'zero-element/lib/utils/hooks/lifeCycle';

/**
 * 获取详情数据
 *
 * @export
 * @param {string} namespace
 * @param {string} API
 * @returns [detailsData, loading]
 */
export default function useDetails(namespace, API) {
  const model = useModel({
    namespace,
  });
  const { formData = {}, loading } = model;

  useDidMount(getData);
  useWillUnmount(clearData);

  function getData() {
    if (API) {
      model.fetchOne({
        API,
      })
    }
  }
  function clearData() {
    // dispatch({
    //   type: 'save',
    //   payload: {
    //     details: {},
    //   }
    // });
  }

  return [
    formData,
    loading,
  ]
}