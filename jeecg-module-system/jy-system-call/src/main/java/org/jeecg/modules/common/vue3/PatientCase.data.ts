import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '就诊卡id',
    align:"center",
    dataIndex: 'cardId'
   },
   {
    title: '预约id',
    align:"center",
    dataIndex: 'appointId'
   },
   {
    title: '医生id',
    align:"center",
    dataIndex: 'doctorId'
   },
   {
    title: '病历详情',
    align:"center",
    dataIndex: 'caseDetail'
   },
   {
    title: '就诊结果;',
    align:"center",
    dataIndex: 'diagnosisResult'
   },
   {
    title: '就诊费用;',
    align:"center",
    dataIndex: 'diagnosisFee'
   },
   {
    title: '支付状态（缴费状态（0：未缴费，1：已缴费））;',
    align:"center",
    dataIndex: 'paymentStatus'
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
    label: '就诊卡id',
    field: 'cardId',
    component: 'Input',
  },
  {
    label: '预约id',
    field: 'appointId',
    component: 'Input',
  },
  {
    label: '医生id',
    field: 'doctorId',
    component: 'Input',
  },
  {
    label: '病历详情',
    field: 'caseDetail',
    component: 'Input',
  },
  {
    label: '就诊结果;',
    field: 'diagnosisResult',
    component: 'Input',
  },
  {
    label: '就诊费用;',
    field: 'diagnosisFee',
    component: 'InputNumber',
  },
  {
    label: '支付状态（缴费状态（0：未缴费，1：已缴费））;',
    field: 'paymentStatus',
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