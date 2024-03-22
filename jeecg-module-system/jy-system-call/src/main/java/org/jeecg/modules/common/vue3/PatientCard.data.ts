import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '就诊openId',
    align:"center",
    dataIndex: 'openId'
   },
   {
    title: '姓名',
    align:"center",
    dataIndex: 'realname'
   },
   {
    title: '性别(0-默认未知,1-男,2-女)',
    align:"center",
    dataIndex: 'sex'
   },
   {
    title: '电话',
    align:"center",
    dataIndex: 'phone'
   },
   {
    title: '出生日期',
    align:"center",
    dataIndex: 'birthday'
   },
   {
    title: '证件',
    align:"center",
    dataIndex: 'credentials'
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
    label: '就诊openId',
    field: 'openId',
    component: 'Input',
  },
  {
    label: '姓名',
    field: 'realname',
    component: 'Input',
  },
  {
    label: '性别(0-默认未知,1-男,2-女)',
    field: 'sex',
    component: 'InputNumber',
  },
  {
    label: '电话',
    field: 'phone',
    component: 'Input',
  },
  {
    label: '出生日期',
    field: 'birthday',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '证件',
    field: 'credentials',
    component: 'Input',
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