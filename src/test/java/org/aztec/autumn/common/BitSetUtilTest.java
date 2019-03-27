package org.aztec.autumn.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aztec.autumn.common.utils.BitSetUtil;
import org.aztec.autumn.common.utils.FileUtils;
import org.aztec.autumn.common.utils.BitSetUtil.BitSetData;
import org.aztec.autumn.common.utils.jxl.MSExcelUtil;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;

public class BitSetUtilTest {

	private static int[][] warehouseManageRes = new int[][] {
			// Ա��ģ��
			{
				132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 144, 259,
				145, 146, 148, 149, 151, 174, 175, 178, 179, 348, 349, 35,
				351, 313, 314, 315, 355, 342, 337, 338, 186, 187, 188, 189,
				19, 274, 191, 192, 296, 3, 328, 329, 34, 316, 321, 323,
				335, 336, 339, 193, 194, 195, 196, 282, 331, 332, 198, 2,
				22, 23, 27, 29, 21, 212, 215, 216, 217, 218, 219, 22, 221,
				222, 223, 241, 242, 243, 229, 283, 284, 287, 288, 289, 29,
				31, 365, 358, 21, 273, 24, 25, 26, 28, 211, 213, 214, 245,
				246, 26, 291, 294, 31, 32, 343, 356, 357,

				225, 226, 227, 228, 275, 276, 285, 286, 297, 298, 346, 347,
				354, 33, 231, 232, 233, 234, 235, 237, 238, 244, 366, 387,
				38, 381, 382, 383, 385, 386, 247, 248, 249, 25, 251, 252,
				253, 254, 255, 256, 257, 261, 268, 269, 27, 277, 278, 279,
				28, 293, 33, 317, 318, 319, 32, 325, 326, 327, 333, 34,
				345, 36, 362, 363, 367, 128, 129, 131, 176, 184, 185, 197,
				199, 224, 230, 236, 183, 127, 183,400,401,402,403,404,405,
				406,407,408
			},
			// �⹤ģ��
			{

			},
			// �ⲿ�˺�ģ��
			{
				
			},
			// �ֿ�����
			{ 132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 144, 259,
					145, 146, 148, 149, 151, 174, 175, 178, 179, 348, 349, 35,
					351, 313, 314, 315, 355, 342, 337, 338, 186, 187, 188, 189,
					19, 274, 191, 192, 296, 3, 328, 329, 34, 316, 321, 323,
					335, 336, 339, 193, 194, 195, 196, 282, 331, 332, 198, 2,
					22, 23, 27, 29, 21, 212, 215, 216, 217, 218, 219, 22, 221,
					222, 223, 241, 242, 243, 229, 283, 284, 287, 288, 289, 29,
					31, 365, 358, 21, 273, 24, 25, 26, 28, 211, 213, 214, 245,
					246, 26, 291, 294, 31, 32, 343, 356, 357,

					225, 226, 227, 228, 275, 276, 285, 286, 297, 298, 346, 347,
					354, 33, 231, 232, 233, 234, 235, 237, 238, 244, 366, 387,
					38, 381, 382, 383, 385, 386, 247, 248, 249, 25, 251, 252,
					253, 254, 255, 256, 257, 261, 268, 269, 27, 277, 278, 279,
					28, 293, 33, 317, 318, 319, 32, 325, 326, 327, 333, 34,
					345, 36, 362, 363, 367, 128, 129, 131, 176, 184, 185, 197,
					199, 224, 230, 236, 183, 127, 183 },
			// �����ƻ��鳤
			{ 132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 144, 259,
					145, 146, 148, 149, 151, 174, 175, 178, 179, 348, 349, 35,
					351, 313, 314, 315, 355, 342, 337, 338, 186, 187, 188, 189,
					19, 274, 191, 192, 296, 3, 328, 329, 34, 316, 321, 323,
					335, 336, 339, 193, 194, 195, 196, 282, 331, 332, 198, 2,
					22, 23, 27, 29, 21, 212, 215, 216, 217, 218, 219, 22, 221,
					222, 223, 241, 242, 243, 229, 283, 284, 287, 288, 289, 29,
					361, 31, 365, 324, 358, 21, 273, 24, 25, 26, 28, 211, 213,
					214, 245, 246, 26, 291, 294, 31, 32, 343, 356, 357, 225,
					226, 227, 228, 275, 276, 285, 286, 297, 298, 346, 347, 354,
					33, 231, 232, 233, 234, 235, 237, 238, 244, 366, 387, 38,
					381, 382, 383, 385, 386, 247, 248, 249, 25, 251, 252, 253,
					254, 255, 256, 257, 261, 268, 269, 27, 277, 278, 279, 28,
					293, 33, 317, 318, 319, 32, 325, 326, 327, 333, 34, 345,
					36, 362, 363, 367, 128, 129, 131, 176, 184, 185, 197, 199,
					224, 230, 236, 183, 127 },
			// �����ƻ��Ƶ�,
			{ 143, 174, 175, 348, 349, 35, 351, 313, 314, 315, 355, 342, 337,
					338, 186, 19, 3, 328, 34, 323, 336, 193, 195, 196, 331,
					198, 2, 22, 23, 27, 29, 21, 212, 215, 216, 217, 218, 219,
					22, 221, 222, 223, 241, 242, 243, 229, 283, 284, 287, 288,
					289, 29, 361, 31, 365, 324, 358, 21, 273, 24, 25, 26, 28,
					211, 213, 214, 245, 246, 26, 291, 294, 31, 32, 343, 356,
					357, 225, 226, 227, 228, 275, 276, 285, 286, 292, 297, 298,
					346, 347, 354, 33, 231, 235, 237, 238, 244, 366, 387, 38,
					381, 382, 383, 385, 386, 247, 248, 249, 25, 251, 252, 253,
					254, 255, 256, 257, 261, 268, 269, 27, 277, 278, 279, 28,
					293, 33, 317, 318, 319, 32, 325, 326, 327, 333, 34, 345,
					36, 362, 363, 367, 129, 128, 184, 185, 197, 199, 224, 230,
					236, 176, 183, 127 },
			// ���˰��鳤
			{ 132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 144, 259,
					145, 146, 151, 174, 175, 178, 179, 348, 349, 35, 351, 313,
					314, 315, 355, 342, 337, 186, 187, 188, 189, 19, 274, 191,
					192, 296, 3, 328, 329, 34, 316, 321, 323, 335, 336, 339,
					193, 194, 195, 196, 282, 331, 332, 198, 2, 22, 23, 27, 29,
					21, 212, 215, 217, 218, 219, 222, 223, 241, 242, 243, 21,
					273, 24, 25, 26, 28, 211, 213, 214, 245, 246, 26, 291, 294,
					31, 32, 343, 356, 357, 225, 226, 227, 228, 275, 276, 285,
					286, 292, 297, 298, 346, 347, 354, 33, 231, 232, 233, 234,
					235, 322, 237, 238, 244, 366, 387, 38, 381, 382, 383, 385,
					386, 247, 248, 249, 25, 251, 252, 253, 254, 255, 256, 257,
					261, 268, 269, 27, 277, 278, 279, 28, 293, 33, 317, 318,
					319, 32, 325, 326, 327, 333, 34, 345, 36, 362, 363, 367,
					128, 129, 131, 176, 184, 185, 197, 199, 224, 230, 236, 183,
					127 },
			// ����Ա��
			{ 143, 186, 187, 188, 189, 19, 274, 191, 192, 296, 3, 328, 329, 34,
					316, 321, 323, 335, 336, 339, 21, 273, 24, 25, 28, 213, 31,
					32, 343, 356, 357, 225, 226, 227, 298, 232, 129, 184, 199,
					224, 230, 185, 183, 128, 127 },
			// �泵���¼ܰ��鳤
			{ 132, 133, 134, 135, 136, 137, 138, 143, 144, 259, 145, 146, 174,
					186, 187, 188, 189, 19, 274, 191, 192, 296, 3, 328, 329,
					34, 316, 321, 323, 335, 336, 339, 198, 21, 273, 24, 25, 26,
					28, 211, 213, 214, 245, 246, 26, 291, 294, 31, 32, 343,
					356, 357, 225, 226, 227, 228, 275, 276, 285, 286, 292, 297,
					298, 346, 347, 354, 33, 231, 232, 233, 234, 235, 237, 238,
					244, 366, 387, 38, 381, 382, 383, 385, 386, 28, 326, 367,
					128, 129, 131, 184, 197, 199, 224, 230, 236, 185, 176, 183,
					127 },
			// �泵���¼�Ա��
			{},
			// �洢���鳤
			{ 133, 134, 136, 144, 259, 145, 146, 174, 175, 225, 226, 227, 228,
					275, 276, 285, 286, 297, 298, 346, 347, 354, 33, 128, 129,
					131, 224, 127, 183 },
			// �洢Ա��
			{},
			// �������鳤
			{ 144, 259, 174, 313, 314, 342, 324, 358, 21, 273, 24, 25, 26, 28,
					211, 213, 214, 245, 246, 26, 291, 294, 31, 32, 343, 356,
					357, 225, 226, 227, 228, 275, 276, 285, 286, 292, 297, 298,
					346, 347, 354, 33, 255, 256, 27, 271, 272, 277, 278, 28,
					281, 293, 33, 345, 127, 183, 129, 199, 224, 176, 184, 197 },
			// ����Ա��
			{},
			// ��ά���鳤
			{ 132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 144, 259,
					145, 151, 174, 175, 178, 179, 313, 314, 315, 355, 342, 186,
					188, 189, 19, 274, 191, 192, 296, 3, 328, 329, 34, 316,
					321, 323, 335, 336, 339, 198, 2, 22, 23, 27, 29, 21, 212,
					215, 216, 217, 218, 219, 222, 223, 241, 243, 229, 288, 358,
					21, 273, 24, 25, 26, 28, 211, 213, 214, 245, 246, 26, 291,
					294, 31, 32, 343, 356, 357, 225, 226, 227, 228, 275, 276,
					285, 286, 297, 298, 346, 347, 354, 33, 231, 232, 234, 237,
					238, 244, 366, 387, 38, 381, 382, 383, 385, 386, 247, 248,
					249, 25, 251, 252, 253, 254, 255, 256, 257, 261, 268, 269,
					27, 277, 278, 279, 28, 293, 33, 317, 318, 319, 32, 325,
					326, 327, 333, 34, 345, 36, 362, 363, 367, 128, 129, 131,
					176, 184, 197, 199, 224, 230, 236, 185, 183, 127, 183 },
			// ��άԱ��
			{},
			// ������鳤
			{ 217, 218, 197 },
			// ���Ա��
			{},
			// ��װ���鳤
			{},
			// ��װԱ��
			{},
			// ���ӷ����鳤
			{},
			// ���ӷ���Ա��
			{},
			// �����������鳤
			{ 132, 133, 134, 135, 136, 137, 138, 14, 141, 142, 143, 174, 355,
					189, 191, 198, 2, 22, 23, 27, 29, 21, 212, 215, 216, 217,
					218, 219, 22, 221, 222, 223, 241, 242, 243, 229, 283, 284,
					287, 288, 289, 29, 31, 365, 358, 21, 273, 24, 25, 245, 31,
					32, 343, 356, 357, 225, 226, 227, 228, 275, 276, 285, 286,
					297, 298, 346, 347, 354, 33, 232, 234, 237, 238, 244, 366,
					387, 38, 381, 382, 383, 385, 386, 247, 248, 249, 25, 251,
					252, 253, 254, 255, 256, 257, 261, 268, 269, 27, 277, 278,
					279, 28, 293, 33, 317, 318, 319, 32, 325, 326, 327, 333,
					34, 345, 36, 362, 363, 367, 128, 129, 184, 197, 199, 224,
					230, 236, 176, 127, 183 },
			// ��������Ա��
			{},
			// �쳣���鳤
			{ 132, 133, 134, 135, 136, 137, 138, 141, 142, 143, 144, 259, 174,
					175, 178, 179, 348, 349, 35, 351, 189, 191, 198, 2, 22, 23,
					27, 29, 212, 215, 217, 218, 219, 22, 221, 222, 223, 241,
					242, 243, 229, 283, 284, 287, 288, 289, 29, 31, 365, 358,
					21, 273, 24, 25, 28, 211, 213, 214, 245, 246, 26, 291, 294,
					31, 32, 343, 356, 357, 225, 226, 227, 228, 275, 276, 285,
					286, 297, 298, 346, 347, 354, 33, 232, 237, 387, 127, 183,
					128, 129, 176, 184, 197, 199, 224, 230, 236 },
			// �쳣Ա��
			{} };

	private static int[] rfMenuIds = new int[] { 121, 122, 130, 153, 157, 164,
			168, 172, 374, 123, 154, 155, 156, 158, 159, 160, 161, 162, 163,
			165, 166, 167, 169, 170, 171, 173, 375, 376, 377, 378, 368, 369,
			370, 371, 372, 373 };

	private static String[] ROLE_CODES = { "wh_admin_tpl", "scjh_leader_tpl",
			"scjh_staff_tpl", "st_leader_tpl", "st_staff_tpl",
			"ccsxj_leader_tmpl", "ccsxj_staff_tmpl", "storage_leader_tpl",
			"storage_staff_tpl", "bh_leader_tpl", "bh_staff_tpl",
			"hw_leader_tpl", "hw_staff_tpl", "jh_leader_tpl", "jh_staff_tpl",
			"pack_leader_tpl", "pack_staff_tpl", "jj_leader_tpl",
			"jj_staff_tpl", "ddsc_leader_tpl", "ddsc_staff_tpl",
			"expt_leader_tpl", "expt_staff_tpl" };

	public static void main(String[] args) {
		// testUtil(10000);
		// testLong();
		// outprintRoleResources();

		/*
		 * List<String> bsStrings = getBitSetStrs(); for(String bsString :
		 * bsStrings){ testSetBit(bsString, rfMenuIds); }
		 */
		// testSetBit("0_-9223372036854775808_252553422853734402_-325103735673979906_-290525409110724686_-181269936575365697_58785903(0)",
		// rfMenuIds);
		//testSetBit("0_0_0_0_0_394909392325050368(0)", rfMenuIds);
		//outprintSqlForInsert();
		//
		try {
			genUpdateSql();
			//genSelectMenuSql();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testSetBit(String menuAcl, int[] pocs) {
		BitSet bitData = BitSetUtil.readBitSet(menuAcl).getData();
		System.out.println("testing ---" + menuAcl);
		for (int i = 0; i < pocs.length; i++) {
			boolean bitSetGetResult = bitData.get(pocs[i]);
			// System.out.println("poc[" + pocs[i] + "]:" + bitSetGetResult);
			if (!bitSetGetResult) {
				System.err.println("poc[" + pocs[i] + "] is not satified!");
			}
		}
		System.out.println("testing ---" + menuAcl + " finished!");
	}
	
	
	public static void outprintSqlForInsert(){

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("INSERT INTO `base_auth_role`(`gmt_create`,`gmt_modified`,`name`,`status`,`remark`,`feature`,`type`,`code`,`parent_id`,`role_group`,`belong_object`,`user_type`,`menu_acl`,`action_acl`,`version`,`is_template`,`source_id`) values");
		
		sqlBuilder.append("(now(),now(),'ϵͳԱ��ģ��',2,'','',1,'wh_admin_tpl',0,0,0,2,'#R1#','(0)',1,1,0),");
		sqlBuilder.append("(now(),now(),'ϵͳ�⹤ģ��',2,'','',1,'wh_admin_tpl',0,0,0,1,'#R2#','(0)',1,1,0),");
		sqlBuilder.append("(now(),now(),'ϵͳ�ⲿ�˺�ģ��',2,'','',1,'wh_admin_tpl',0,0,0,3,'#R3#','(0)',1,1,0);");
//		sqlBuilder
//				.append("(now(),now(),'�ֿ�����ģ��',1,'','',1,'wh_admin_tpl',0,0,0,2,'#R1#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�����ƻ��鳤ģ��',1,'','',1,'scjh_leader_tpl',0,0,0,2,'#R2#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�����ƻ��Ƶ�ģ��',1,'','',1,'scjh_staff_tpl',0,0,0,2,'#R3#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'���˰��鳤ģ��',1,'','',1,'st_leader_tpl',0,0,0,2,'#R4#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'����Ա��ģ��',1,'','',1,'st_staff_tpl',0,0,0,2,'#R5#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�泵���¼ܰ��鳤ģ��',1,'','',1,'ccsxj_leader_tmpl',0,0,0,2,'#R6#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�泵���¼�Ա��ģ��',1,'','',1,'ccsxj_staff_tmpl',0,0,0,2,'#R7#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�洢���鳤ģ��',1,'','',1,'storage_leader_tpl',0,0,0,2,'#R8#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�洢Ա��ģ��',1,'','',1,'storage_staff_tpl',0,0,0,2,'#R9#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�������鳤ģ��',1,'','',1,'bh_leader_tpl',0,0,0,2,'#R10#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'����Ա��ģ��',1,'','',1,'bh_staff_tpl',0,0,0,2,'#R11#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'��ά���鳤ģ��',1,'','',1,'hw_leader_tpl',0,0,0,2,'#R12#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'��άԱ��ģ��',1,'','',1,'hw_staff_tpl',0,0,0,2,'#R13#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'������鳤ģ��',1,'','',1,'jh_leader_tpl',0,0,0,2,'#R14#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'���Ա��ģ��',1,'','',1,'jh_staff_tpl',0,0,0,2,'#R15#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'��װ���鳤ģ��',1,'','',1,'pack_leader_tpl',0,0,0,2,'#R16#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'��װԱ��ģ��',1,'','',1,'pack_staff_tpl',0,0,0,2,'#R17#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'���ӷ����鳤ģ��',1,'','',1,'jj_leader_tpl',0,0,0,2,'#R18#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'���ӷ���Ա��ģ��',1,'','',1,'jj_staff_tpl',0,0,0,2,'#R19#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�����������鳤ģ��',1,'','',1,'ddsc_leader_tpl',0,0,0,2,'#R20#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'��������Ա��ģ��',1,'','',1,'ddsc_staff_tpl',0,0,0,2,'#R21#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�쳣���鳤ģ��',1,'','',1,'expt_leader_tpl',0,0,0,2,'#R22#','(0)',1,1,0),");
//		sqlBuilder
//				.append("(now(),now(),'�쳣Ա��ģ��',1,'','',1,'expt_staff_tpl',0,0,0,2,'#R23#','(0)',1,1,0),");
		String insertSql = sqlBuilder.toString();
		//for (int i = 0; i < warehouseManageRes.length; i++) {
		for (int i = 0; i < 3; i++) {
			int[] resList = warehouseManageRes[i];
			BitSet bitSetData = new BitSet();
			for (int j = 0; j < resList.length; j++) {
				bitSetData.set(resList[j]);
			}
			for (int j = 0; j < rfMenuIds.length; j++) {
				bitSetData.set(rfMenuIds[j]);
			}
			String aclStr = BitSetUtil.toString(bitSetData, 0);
			insertSql = insertSql.replace("#R" + (i + 1) + "#",aclStr);
			/*sqlBuilder.append("update base_auth_role set menu_acl = '" + aclStr
					+ "' where code='" + ROLE_CODES[i] + "';\n");*/
		}
		System.out.println(insertSql);
	}

	public static void outprintRoleResourcesForUpdate() {
		StringBuilder sqlBuilder = new StringBuilder();
		

		for (int i = 0; i < warehouseManageRes.length; i++) {
			int[] resList = warehouseManageRes[i];
			BitSet bitSetData = new BitSet();
			for (int j = 0; j < resList.length; j++) {
				bitSetData.set(resList[j]);
			}
			for (int j = 0; j < rfMenuIds.length; j++) {
				bitSetData.set(rfMenuIds[j]);
			}
			String aclStr = BitSetUtil.toString(bitSetData, 0);
			// System.out.println("ROLE-" + i + ":" +
			// BitSetUtil.toString(bitSetData, 0));
			// insertSql = insertSql.replace("#R" + (i + 1) + "#",
			// BitSetUtil.toString(bitSetData, 0));
			sqlBuilder.append("update base_auth_role set menu_acl = '" + aclStr
					+ "' where code='" + ROLE_CODES[i] + "';\n");
		}
		// System.out.println(insertSql);
		System.out.println(sqlBuilder.toString());
	}

	private static List<String> getBitSetStrs() {
		List<String> bitSetString = new ArrayList<>();
		for (int i = 0; i < warehouseManageRes.length; i++) {
			int[] resList = warehouseManageRes[i];
			BitSet bitSetData = new BitSet();
			for (int j = 0; j < resList.length; j++) {
				bitSetData.set(resList[j]);
			}
			for (int j = 0; j < rfMenuIds.length; j++) {
				bitSetData.set(rfMenuIds[j]);
			}
			String aclStr = BitSetUtil.toString(bitSetData, 0);
			bitSetString.add(aclStr);
		}
		return bitSetString;
	}

	public static void testUtil(int testTime) {
		for (int k = 0; k < testTime; k++) {
			System.out
					.println("##################TEST BITSET##################");
			List<Integer> locations = new ArrayList<Integer>();
			int testAmount = 10, version = 3;
			Random random = new Random();
			for (int i = 0; i < testAmount; i++) {
				int randInt = random.nextInt(10000);
				while (randInt <= 0 || randInt == Integer.MAX_VALUE
						|| randInt == Integer.MIN_VALUE) {
					randInt = random.nextInt(10000);
				}
				locations.add(randInt);
			}
			// Integer[] errData = {95355, 51790, 34506, 70213, 43007, 31814,
			// 42340, 83372, 31634, 4473};
			/*
			 * for(int i = 0;i <errData.length;i++){ locations.add(errData[i]);
			 * }
			 */
			System.out.println(locations);
			BitSet bitSet = BitSetUtil.array2BitSet(locations);
			System.out.println("origin bs:" + bitSet);
			String hexString = BitSetUtil.toString(bitSet, version);
			System.out.println("hStr:" + hexString);
			BitSetData readData = BitSetUtil.readBitSet(hexString);
			BitSet readBs = readData.getData();
			BitSet cloneBs = (BitSet) readBs.clone();
			String readHexString = BitSetUtil.toString(readData.getData(),
					version);
			System.out.println("rStr:" + readHexString);
			cloneBs.xor(readBs);
			/*
			 * if(!readHexString.equals(hexString)){
			 * System.err.print("Err read!target targetHexStr:" +
			 * readHexString); System.exit(-1); }
			 */
			if (cloneBs.nextSetBit(0) != -1) {
				System.err.print("Err read!target bs" + readBs);
				System.exit(-1);
			}
			int readVersion = readData.getVersion();
			System.out.println("read bs:" + readBs);
			System.out.println("read Ver:" + readVersion);
			Collections.sort(locations, new Comparator<Integer>() {

				@Override
				public int compare(Integer arg0, Integer arg1) {
					// TODO Auto-generated method stub
					return arg0 - arg1;
				}
			});
			/*
			 * locations.sort(new Comparator<Integer>() {
			 * 
			 * @Override public int compare(Integer arg0, Integer arg1) { //
			 * TODO Auto-generated method stub return arg0 - arg1; } });
			 */
			String sortStr = locations.toString();
			System.out.println(sortStr);
			List<Integer> transferLocs = BitSetUtil.readSetBits(readData
					.getData());
			String tranStr = transferLocs.toString();
			System.out.println(tranStr);
			if (!sortStr.equals(tranStr)) {
				System.err.println("location read error!");
				System.exit(-1);
			}
			System.out
					.println("##################TEST BITSET##################");
		}
	}

	public static void testLong() {
		// Long errorData = BitSetUtil.hex2Long("8000000000000000
		/*
		 * Long errorData = BitSetUtil.hex2Long("8000000000000000"); Long
		 * errorOutdata = BitSetUtil.hex2Long("7fffffffffffffff");
		 * System.out.println(Long.MAX_VALUE); System.out.println(errorData);
		 * System.out.println(errorOutdata);
		 */
		BitSet testBitSet = new BitSet();
		testBitSet.set(43007);
		System.out.println(BitSetUtil.toString(testBitSet, 1));
		String hexString = BitSetUtil.toString(testBitSet, 1);
		BitSet nextSet = BitSetUtil.readBitSet(hexString).getData();
		System.out.println(BitSetUtil.readSetBits(nextSet));

	}
	
	public static void genUpdateSql() {
		try {
			Long[] roleIds = new Long[] { 218l, 219l, 220l, 221l, 222l, 223l, 224l, 225l, 226l, 227l, 228l, 229l, 230l,
					231l, 232l, 233l, 234l, 235l, 236l, 237l, 238l, 239l, 240l };
			Map<Long,List<Integer>> sqlDatas = Maps.newHashMap();
			String sqlTemplate = "update base_auth_role set menu_acl='{menuAcl}' where id={roleId};";
			List<List<String>> sheet1Data = MSExcelUtil.read(new File("roleResConfig.xls")).get(0);
			for(int i = 2;i < sheet1Data.size();i++) {
				List<String> rowData = sheet1Data.get(i);
				Long menuID = Long.parseLong(rowData.get(0));
				for(int j = 1;j < rowData.size();j++) {
					Long roleId = roleIds[j - 1];
					List<Integer> menuIds = sqlDatas.get(roleId);
					if(menuIds == null ) {
						menuIds = Lists.newArrayList();
					}
					String checkPoint = rowData.get(j);
					if(checkPoint.trim().equals("√")) {
						//System.out.println("add:(" + roleId + "," + menuID + ")" );
						menuIds.add(menuID.intValue());
					}
					sqlDatas.put(roleId, menuIds);
				}
			}
			List<String> sqls = new ArrayList<>();
			for(Long roleId : sqlDatas.keySet()) {
				//System.out.println("Role[id=" + roleId + "]:" + sqlDatas.get(roleId));
				String sql = sqlTemplate.replace("{menuAcl}", BitSetUtil.toString(BitSetUtil.array2BitSet(sqlDatas.get(roleId)), 0));
				sql = sql.replace("{roleId}", "" + roleId);
				sqls.add(sql);
				System.out.println(sql);
			}
			validate(sqlDatas, sqls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void validate(Map<Long,List<Integer>> sqlDatas,List<String> sqls) throws Exception {
		for(String sql : sqls) {
			Long roleId = Long.parseLong(sql.substring((sql.indexOf("id=") + 3),sql.length() - 1));
			Integer index1 = sql.indexOf('\'');
			Integer index2 = sql.indexOf('\'', index1 + 1);
			String menuAcl = sql.substring(index1 + 1,index2);
			if(menuAcl.isEmpty())
				continue;
			List<Integer> location = sqlDatas.get(roleId);
			BitSet bs = BitSetUtil.readBitSet(menuAcl).getData();
			for(Integer loc : location) {
				if(!bs.get(loc))
					throw new Exception("sql generate fail!RoleId:" + roleId + ",menuId:" + loc);
			}
		}
	}
	
	private static String genSelectMenuSql() throws IOException {
		String sqlTemplate = "select id,name from base_auth_menu where name in ({menuNames})";
		String sql = sqlTemplate;
		File menuFile = new File("menuName.txt");
		String fileContent = FileUtils.readFileAsString(menuFile);
		String[] menuNameArr = fileContent.split("\r\n");
		StringBuilder menuNames = new StringBuilder();
		for(String menuName : menuNameArr) {
			if(!menuNames.toString().isEmpty()) {
				menuNames.append(",");
			}
			menuNames.append("'" + menuName + "'");
		}
		sql = sqlTemplate.replace("{menuNames}", menuNames.toString());
		System.out.println(sql);
		return sql;
	}
}
