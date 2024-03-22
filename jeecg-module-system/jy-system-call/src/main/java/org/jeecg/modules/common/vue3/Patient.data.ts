import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '微信openId',
    align:"center",
    dataIndex: 'openId'
   },
   {
    title: '头像',
    align:"center",
    dataIndex: 'avatar'
   },
   {
    title: '姓名',
    align:"center",
    dataIndex: 'realname'
   },
   {
    title: '昵称',
    align:"center",
    dataIndex: 'nickName'
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
    title: '证件',
    align:"center",
    dataIndex: 'credentials'
   },
   {
    title: '生日',
    align:"center",
    dataIndex: 'birthday'
   },
   {
    title: '关注状态（0:待关注,1:已关注，2：已取消,3:已授权）',
    align:"center",
    dataIndex: 'followStatus'
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
    label: '微信openId',
    field: 'openId',
    component: 'Input',
  },
  {
    label: '头像',
    field: 'avatar',
    component: 'Input',
  },
  {
    label: '姓名',
    field: 'realname',
    component: 'Input',
  },
  {
    label: '昵称',
    field: 'nickName',
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
    label: '证件',
    field: 'credentials',
    component: 'Input',
  },
  {
    label: '生日',
    field: 'birthday',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '关注状态（0:待关注,1:已关注，2：已取消,3:已授权）',
    field: 'followStatus',
    component: 'InputNumber',
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