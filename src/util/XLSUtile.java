package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import flatmucf.org.apache.poi.hssf.usermodel.HSSFRow;
import flatmucf.org.apache.poi.hssf.usermodel.HSSFSheet;
import flatmucf.org.apache.poi.hssf.usermodel.HSSFWorkbook;
import flatmucf.org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XLSUtile {
	private static Map<String,ArrayList<Integer>> result = new HashMap<String,ArrayList<Integer>>();
	
	
	private static void writeCellValue(HSSFRow row,int cellNo,String value){
		row.createCell(cellNo).setCellValue(value);
	}

	private static void readCellValue(File inFIle) throws Exception{
		FileInputStream os = new FileInputStream(inFIle);
		POIFSFileSystem poi = new POIFSFileSystem(os);
		HSSFWorkbook workbook = new HSSFWorkbook(poi);
		HSSFSheet sheetInfo = workbook.getSheetAt(0);     
		//�Ա����Ϣ���ռ�
		collectionInfo(sheetInfo);
		HSSFSheet sheetResult = workbook.getSheet("Sheet4");
		//�Խ����ͳ��
		calculateResult(workbook,sheetResult,inFIle);
		
		os.close();
	}
	
	/**
	 * ͳ�Ƹ���ָ�����ڷ���
	 * @param workbook
	 * @param sheet
	 * @param file
	 * @throws Exception
	 */
	private static void calculateResult(HSSFWorkbook workbook,HSSFSheet sheet,File file) throws Exception {
		if(sheet==null){
			throw new Exception("û�����sheet!");
		}
		HSSFRow ro = null;
		for(int i = 1;sheet.getRow(i)!=null;i++){
			ro = sheet.getRow(i);
			if(ro.getCell(0)!=null){
				if(ro.getCell(2)!=null&&!"".equals(ro.getCell(2).toString())){
					ro.getCell(1).setCellValue(ro.getCell(2).toString());
				}
				String name = ro.getCell(0).toString();
				if(result.get(name)!=null){
					String currentCoveryNum = result.get(name).get(0).toString()+"/"+result.get(name).get(1).toString();
					writeCellValue(ro,2,currentCoveryNum);
					//�������µĸ�����
					int resultNum = calculateCurrentCovery(ro,result.get(name));
					//�������ֵ�����ʵķ���
					calculateAbsScore(ro,resultNum);
					//���㸲���ʽ����ķ���
					calculateProgressScore(ro);
				}
				
			}
		}
		FileOutputStream os = new FileOutputStream(file);
		workbook.write(os);
		os.flush();
		os.close();
	}

	/**
	 * ��������
	 * @param num
	 * @param scale
	 * @return
	 */
	private static BigDecimal halfNum(BigDecimal num,int scale){
		return num.divide(new BigDecimal(1), scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * ������������������������վ�ȷ��λС�������㣬�Ͱ���ȡ�������㣬����̫�鷳
	 * @param ro
	 * @throws Exception
	 */
	private static void calculateProgressScore(HSSFRow ro) throws Exception {
		BigDecimal currentNum = new BigDecimal(ro.getCell(4).toString()).multiply(new BigDecimal(100));
		BigDecimal baseNum = new BigDecimal(ro.getCell(3).toString()).multiply(new BigDecimal(100));
		BigDecimal progressNum = currentNum.subtract(baseNum);
		if(progressNum.intValue()<0){
			return;
		}
//		BigDecimal resultTemp = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		BigDecimal progress = new BigDecimal(0.1);
		for(BigDecimal i= new BigDecimal(0.1);i.compareTo(progressNum)<=0;i=i.add(progress)){
			BigDecimal temp = baseNum.add(i);
			//�����÷ֵ��㷨
			if(temp.compareTo(new BigDecimal(30))<0){
//				resultTemp = resultTemp.add(progress);
				result = result.add(progress.multiply(new BigDecimal(0.2)));
			}else if(temp.compareTo(new BigDecimal(30))>=0&&temp.compareTo(new BigDecimal(50))<0){
				result = result.add(progress.multiply(new BigDecimal(0.5)));
			}else if(temp.compareTo(new BigDecimal(50))>=0&&temp.compareTo(new BigDecimal(70))<0){
				result = result.add(progress.multiply(new BigDecimal(1)));
			}else if(temp.compareTo(new BigDecimal(70))>=0&&temp.compareTo(new BigDecimal(80))<0){
				result = result.add(progress.multiply(new BigDecimal(2)));
			}else if(temp.compareTo(new BigDecimal(80))>=0&&temp.compareTo(new BigDecimal(100))<=0){
				result = result.add(progress.multiply(new BigDecimal(5)));
			}
			
		}
		result = halfNum(result, 1);
		writeCellValue(ro, 8, result.toString());
		
		
	}
	
	/**
	 * ͳ�ƾ���ֵ�÷ֵ�������
	 * @author Ji Cheng 
	 * @date 2016��4��11��
	 * @param ro
	 * @param resultNum
	 * @throws Exception
	 * @return void
	 */
	private static void calculateAbsScore(HSSFRow ro, int resultNum) throws Exception {
		BigDecimal adsResult = halfNum(new BigDecimal(resultNum-60).multiply(new BigDecimal(0.4)),1);
		writeCellValue(ro, 7, adsResult.toString());
	}
	/**
	 * �����ϵĸ����ʣ����������µĸ�����
	 * @param ro
	 * @param list
	 * @throws Exception
	 */
	private static Integer calculateCurrentCovery(HSSFRow ro,ArrayList<Integer> list) throws Exception {
		if(ro.getCell(4)!=null&&!"".equals(ro.getCell(4))){
			ro.getCell(3).setCellValue(ro.getCell(4).toString());
		}
		if(ro.getCell(6)!=null&&!"".equals(ro.getCell(6))){
			ro.getCell(5).setCellValue(ro.getCell(6).toString());
		}
		BigDecimal resultNum = new BigDecimal(list.get(0)).divide(new BigDecimal(list.get(1)),4,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal resultNum1 = halfNum(resultNum.multiply(new BigDecimal(100)),0);
		writeCellValue(ro, 4, resultNum.toString());
		writeCellValue(ro, 6, resultNum1.toString());
		return resultNum1.intValue();
	}
	
	/**
	 * �ռ�sheet1�еĸ���������ָ�꣬���浽һ��map��������ʹ��
	 * @author Ji Cheng 
	 * @date 2016��4��11��
	 * @param sheet
	 * @throws Exception
	 * @return void
	 */
	private static void collectionInfo(HSSFSheet sheet) throws Exception {
		if(sheet==null){
			throw new Exception("û�����sheet!");
		}
		HSSFRow ro = null;
		for(int i=1;sheet.getRow(i)!=null;i++){
			ro = sheet.getRow(i);
			if(ro.getCell(1)==null||ro.getCell(2)==null){
				continue;
			}
			String nameValue = ro.getCell(1).toString().trim();
			String numValue = ro.getCell(2).toString().trim();
			ArrayList<Integer> infoList = result.get(nameValue);
			if(infoList == null){
				infoList = new ArrayList<Integer>();
				infoList.add(0);infoList.add(0);
				result.put(nameValue, infoList);
			}
			String[] values = numValue.split("/");
			if(values!=null&&values.length==2){
				int num1 = infoList.get(0)+Integer.parseInt(values[0]);
				int num2 = infoList.get(1)+Integer.parseInt(values[1]);
				infoList.set(0, num1);
				infoList.set(1, num2);
			}
		}
	}

	public static void main(String[] args) {
		try {
			readCellValue(new File("D:\\DailyWork\\coveryWork\\�����ʰ����Ǳ���׼��.xls"));
			System.out.println(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
