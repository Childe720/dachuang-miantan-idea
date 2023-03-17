
package drug.file;

import drug.dao.Data;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Excel {
    public Excel(String s){

    }
    public void exportData(Data data, JSONObject json) throws JSONException, IOException {
        HSSFWorkbook wb =new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet0");
        //д��ͷ
        JSONArray arrayTitle = json.getJSONArray("aaFieldName");
        HSSFRow rowTitle =sheet.createRow(0);
        for (int i = 0; i < arrayTitle.length(); i++) {
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue((String)arrayTitle.get(i));
        }
        JSONArray array= json.getJSONArray("aaData");
        for (int i = 1; i < array.length(); i++) {
            HSSFRow row =sheet.createRow(i);
            int j=0;
            HashMap<String,String>record=(HashMap<String,String>)array.get(i-1);
            for (HashMap.Entry<String,String>entry:record.entrySet()){
                System.out.println("key="+entry.getKey()+"---value="+entry.getValue());
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(entry.getValue());
                j++;
            }

        }

        FileOutputStream output =new FileOutputStream(json.getString("file_path"));
        wb.write(output);
        output.flush();
        output.close();




    }
}

