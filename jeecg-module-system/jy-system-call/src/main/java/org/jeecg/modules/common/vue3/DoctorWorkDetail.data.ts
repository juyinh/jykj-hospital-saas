import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '值班id',
    align:"center",
    dataIndex: 'doctorWordId'
   },
   {
    title: '出诊时段（1：全天，2：上午，3：下午）',
    align:"center",
    dataIndex: 'workPeriod'
   },
   {
    title: '号源总数',
    align:"center",
    dataIndex: 'numTotal'
   },
   {
    title: '开始就诊时间',
    align:"center",
    dataIndex: 'beginTimeClinic'
   },
   {
    title: '结束就诊时间',
    align:"center",
    dataIndex: 'endTimeClinic'
   },
   {
    title: '租户号',
    align:"center",
    dataIndex: 'tenantId'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '值班id',
    field: 'doctorWordId',
    component: 'Input',
  },
  {
    label: '出诊时段（1：全天，2：上午，3：下午）',
    field: 'workPeriod',
    component: 'Input',
  },
  {
    label: '号源总数',
    field: 'numTotal',
    component: 'InputNumber',
  },
  {
    label: '开始就诊时间',
    field: 'beginTimeClinic',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '结束就诊时间',
    field: 'endTimeClinic',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '租户号',
    field: 'tenantId',
    component: 'InputNumber',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];



/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}