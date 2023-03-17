package drug.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;


public class drugDao {
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"][drug/dao/Db]"+msg);
	}
	/*添加记录*/
	public void addDeviceRecord(Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String name=data.getParam().has("name")?data.getParam().getString("name"):null;
		String type=data.getParam().has("type")?data.getParam().getString("type"):null;
		String price=data.getParam().has("price")?data.getParam().getString("price"):null;
		String hot=data.getParam().has("hot")?data.getParam().getString("hot"):null;
		if(id!=null && name!=null){
			String sql="insert into xm08_drug(id,name,type,price,hot)";
			sql=sql+" values('"+id+"'";
			sql=sql+" ,'"+name+"'";
			sql=sql+" ,'"+type+"'";
			sql=sql+" ,'"+price+"'";
			sql=sql+" ,'"+hot+"')";
			data.getParam().put("sql",sql);
			showDebug(sql);
			updateRecord(data,json);
		}
	}
	/*删除记录*/
	public void deleteDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="delete from xm08_drug where id="+data.getParam().getString("id");
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*修改记录*/
	public void modifyDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String name=data.getParam().has("name")?data.getParam().getString("name"):null;
		String type=data.getParam().has("type")?data.getParam().getString("type"):null;
		String price=data.getParam().has("price")?data.getParam().getString("price"):null;
		String hot=data.getParam().has("hot")?data.getParam().getString("hot"):null;
		if(id!=null){
			String sql="update xm08_drug";
			sql=sql+" set name='"+name+"'";
			sql=sql+" ,type='"+type+"'";
			sql=sql+" ,price='"+price+"'";
			sql=sql+" ,hot='"+hot+"'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*查询记录*/
	public void getDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetRecordSql(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*
	 * 这是一个样板的函数，可以拷贝做修改用
	 */
	public void updateRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		JSONObject param=data.getParam();
		int resultCode=0;
		String resultMsg="ok";
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db updateDb = new Db("wechatApp2023");
		String sql=data.getParam().getString("sql");
		showDebug("[updateRecord]"+sql);
		updateDb.executeUpdate(sql);
		updateDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}
	public void queryRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		List jsonName=new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db queryDb = new Db("wechatApp2023");
		String sql=data.getParam().getString("sql");
		showDebug("[queryRecord]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 0; i < fieldCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
				}
				jsonList.add(map);
			}
			rs.close();
			//加表头信息
			for (int i=0;i<rsmd.getColumnCount();i++){
				String columnLabel=rsmd.getColumnLabel(i+1);
				jsonName.add(columnLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("aaFieldName",jsonName);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}

	public void getDrugCountByType(Data data, JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db queryDb = new Db("yjykfsj2022");
		String sql="select type as type, count(*) as num from xm08_drug group by type";
		showDebug("[queryRecord]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				HashMap map = new HashMap();
				map.put("type",rs.getString("type"));
				map.put("num",rs.getInt("num"));
				jsonList.add(map);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}

	//knowledge_list
	public void get_knowledge_record(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetKnowledge_list(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	private String createGetKnowledge_list(Data data) throws JSONException {
		String sql="select * from knowledge_list";
		return sql;
	}
	//knowledge_type
	public void get_knowledge_type(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetKnowledge_type(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	private String createGetKnowledge_type(Data data) throws JSONException {
		String sql="select * from knowledge_type";
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		sql=sql+" where id="+id;
		return sql;
	}
	//forum_list
	public void get_forum_list(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetForum_list(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	private String createGetForum_list(Data data) throws JSONException {
		String sql="select * from forum";
		String title=data.getParam().has("title")?data.getParam().getString("title"):null;
		if(title!=null && !title.isEmpty())
			sql=sql+" where title like '%"+title+"%'";
		String content=data.getParam().has("content")?data.getParam().getString("content"):null;
		if(content!=null && !content.isEmpty()){
			if(sql.indexOf("where")>-1){
				sql=sql+" or content like '%"+content+"%'";
			}else{
				sql=sql+" where content like '%"+content+"%'";
			}
		}
		return sql;
	}
	//forum_comment
	public void get_comment_record(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetComment_record(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	private String createGetComment_record(Data data) throws JSONException {
		String sql="select * from forum_comment";
		String comment_id=data.getParam().has("comment_id")?data.getParam().getString("comment_id"):null;
		sql=sql+" where comment_id="+comment_id;
		return sql;
	}

	private String createGetRecordSql(Data data) throws JSONException {
		String sql="select * from xm08_drug";
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null && !id.isEmpty())
			sql=sql+" where id="+id;
		String name=data.getParam().has("name")?data.getParam().getString("name"):null;
		if(name!=null && !name.isEmpty()){
			if(sql.indexOf("where")>-1){
				sql=sql+" and name like '%"+name+"%'";
			}else{
				sql=sql+" where name like '%"+name+"%'";
			}
		}
		String type=data.getParam().has("type")?data.getParam().getString("type"):null;
		if(type!=null &&!type.isEmpty()){
			if(sql.indexOf("where")>-1){
				sql=sql+" and type like '%"+type+"%'";
			}else{
				sql=sql+" where type like '%"+type+"%'";
			}
		}
		return sql;
	}

	public void getGpsStatus(Data data, JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		//String timeFrom=(new SimpleDateFormat("yyyy-MM-dd 00:00:00")).format(new Date());
		//String timeTo=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		int totalGpsActiveCount=0;
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);
		json.put("gps_vehicle_active_number",totalGpsActiveCount);
		//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}


	public void SaveMoney(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String money_left=data.getParam().has("money_left")?data.getParam().getString("money_left"):null;
		String money_save=data.getParam().has("money_save")?data.getParam().getString("money_save"):null;
		String money = money_left + money_save;
		if(id!=null){
			String sql="update xm08_user_list";
			sql=sql+" set money='"+money+"'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}

}
