package com.lumex.bluetoothproject.graphic;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lumex.bluetoothproject.R;
import com.lumex.bluetoothproject.util.db.Command;
import com.lumex.bluetoothproject.util.db.CommandDao;
import com.lumex.bluetoothproject.util.db.DBManager;

/**
 * Created by 阿泰Charles on 2016/12/03.
 */

public class Graphic extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private ImageView ivPreview;
    private RadioGroup rgTabBar;
    private RadioButton rbText;
    private RadioButton rbColor;
    private RadioButton rbDelay;
    private RadioButton rbKeyboard;
    private RadioButton rbStyle;
    private ViewPager vPager;
    private EditText edtEditor;
    private PerformEdit mPerformEdit;
    private ImageView ivUndo;
    private ImageView ivRedo;
    private MyFragmentPagerAdapter mAdapter;
    private Button btnSave;
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic);


        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rbColor.setChecked(true);
    }

    private void bindViews() {
        rgTabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rbText = (RadioButton) findViewById(R.id.rb_text);
        rbColor = (RadioButton) findViewById(R.id.rb_color);
        rbDelay = (RadioButton) findViewById(R.id.rb_delay);
        rbKeyboard = (RadioButton) findViewById(R.id.rb_keyboard);
        rbStyle = (RadioButton) findViewById(R.id.rb_style);
        btnSave = (Button)findViewById(R.id.btn_top_save);
        ivUndo = (ImageView)findViewById(R.id.ib_graphic_undo);
        ivRedo = (ImageView)findViewById(R.id.ib_graphic_redo);
        edtEditor = (EditText)findViewById(R.id.edt_graphic_editor);
        ivPreview = (ImageView)findViewById(R.id.iv_graphic_preview);
        mPerformEdit = new PerformEdit(edtEditor){
            @Override
            protected void onTextChanged(Editable s) {
                //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                super.onTextChanged(s);
            }
        };
        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPerformEdit.undo();
            }
        });
        ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPerformEdit.redo();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Graphic.this);
                LinearLayout commandDialog = (LinearLayout) inflater.inflate(R.layout.graphic_layout_dialog, null);
                final EditText edtCommandCaption = (EditText) commandDialog.findViewById(R.id.edt_graphic_caption);
                //final EditText edtCommandContent = (EditText) commandDialog.findViewById(R.id.edt_command_content);

                new AlertDialog.Builder(Graphic.this).setTitle("添加命令")
                        .setView(commandDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String strCommand = edtCommandCaption.getText().toString();
                                //String strContent = edtCommandContent.getText().toString();
                                String myCommand="";
                                String graphicCommandText = edtEditor.getText().toString();
                                Typeface graphicCommandFont = edtEditor.getTypeface();
                                float graphicCommandSize = edtEditor.getTextSize()-40;
                                int graphicCommandColor = edtEditor.getCurrentTextColor();
                                System.out.println("text: "+graphicCommandText+" font: "+graphicCommandFont+" size: "+graphicCommandSize+" color: "+graphicCommandColor);
                                myCommand = MatrixConversion.BmpToMatrix(ivPreview,128,64,1,20,graphicCommandText, graphicCommandFont,graphicCommandSize, graphicCommandColor);
                                //myCommand = MatrixConversion.BmpToMatrix(ivPreview,128,64,1,20,"1234567890",Typeface.DEFAULT_BOLD,16, Color.BLACK);
                                if (strCommand.equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "command can not be null！" + strCommand, Toast.LENGTH_SHORT).show();
                                } else {
                                    addCommand(null,2, strCommand, myCommand);
                                }
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
                //Toast.makeText(Graphic.this,"save successful",Toast.LENGTH_SHORT).show();
            }
        });
        rgTabBar.setOnCheckedChangeListener(this);
        vPager = (ViewPager) findViewById(R.id.vpager);
        vPager.setAdapter(mAdapter);
        vPager.setCurrentItem(1);
        vPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_keyboard:
                vPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_color:
                vPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_text:
                vPager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_style:
                vPager.setCurrentItem(PAGE_FOUR);
                break;
            case R.id.rb_delay:
                vPager.setCurrentItem(PAGE_FIVE);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vPager.getCurrentItem()) {
                case PAGE_ONE:
                    rbKeyboard.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbColor.setChecked(true);
                    break;
                case PAGE_THREE:
                    rbText.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rbStyle.setChecked(true);
                    break;
                case PAGE_FIVE:
                    rbDelay.setChecked(true);
                    break;
            }
        }
    }

    /**
     * 插入一个新的命令
     * @param id      command的id
     * @param caption command的标题
     * @param content command的内容
     */
    private void addCommand(Long id,int type, String caption, String content) {
        CommandDao commandDao = DBManager.getInstance(this).getDaoSession().getCommandDao();
        Command command = new Command(id, type, caption, content);
        commandDao.insert(command);
    }
}