package com.example.zhouyou.namefight;

/**
 * Created by zhouyou on 16/3/30.
 */

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView guide,result;
    private EditText name;
    private Button getValue,findOpponent,rock,paper,scissor;
    private ListView roleValueList;
    private String nameStr;
    private RoleValue myRoleValue=new RoleValue();
    private int myRoleAttack,myRolehitPoint;
    private List<String> data;
    private ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guide=(TextView)findViewById(R.id.Guide);
        result=(TextView)findViewById(R.id.Result);
        name=(EditText)findViewById(R.id.Name);
        getValue=(Button)findViewById(R.id.getValue);
        findOpponent=(Button)findViewById(R.id.findOpponent);
        rock=(Button)findViewById(R.id.Rock);
        paper=(Button)findViewById(R.id.Paper);
        scissor=(Button)findViewById(R.id.Scissor);
        roleValueList=(ListView)findViewById(R.id.roleValue);

        data=new ArrayList<String>();
        myAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        roleValueList.setAdapter(myAdapter);

//        WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
//        DhcpInfo dhcpInfo=wifiManager.getDhcpInfo();
//        System.out.println(wifiInfo.getIpAddress());
//        System.out.println(long2ip(dhcpInfo.netmask));


        //取得个人战斗值按钮
        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString();
                if (nameStr != null) {
                    //初始化并取得个人战斗值
                    myRoleValue.init_attack(nameStr);
                    myRoleValue.init_hitPoint(nameStr);
                    myRoleAttack = myRoleValue.getattack();
                    myRolehitPoint = myRoleValue.gethitPoint();

                    data.add("ATK" + String.valueOf(myRoleAttack));
                    data.add("HP" + String.valueOf(myRolehitPoint));
                    myAdapter.notifyDataSetChanged();
                } else {
                    //名字不能为空
                    guide.setText("Name cannot be null");
                }
            }
        });

        //1代表石头
        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FightTask myFightTask=new FightTask(rock,paper,scissor,result,myRoleValue);
                myFightTask.execute(1);
            }
        });

        //2代表布
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FightTask myFightTask=new FightTask(rock,paper,scissor,result,myRoleValue);
                myFightTask.execute(2);
            }
        });

        //3代表剪刀
        scissor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FightTask myFightTask=new FightTask(rock,paper,scissor,result,myRoleValue);
                myFightTask.execute(3);
            }
        });
    }

//    public String long2ip(long ip){
//        StringBuffer sb=new StringBuffer();
//        sb.append(String.valueOf((int)(ip&0xff)));
//        sb.append('.');
//        sb.append(String.valueOf((int)((ip>>8)&0xff)));
//        sb.append('.');
//        sb.append(String.valueOf((int)((ip>>16)&0xff)));
//        sb.append('.');
//        sb.append(String.valueOf((int)((ip>>24)&0xff)));
//        return sb.toString();
//    }
}
