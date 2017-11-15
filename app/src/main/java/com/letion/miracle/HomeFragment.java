package com.letion.miracle;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.letion.uikit.dialog.QMUIBottomSheet;
import com.letion.uikit.dialog.QMUIDialog;
import com.letion.uikit.dialog.QMUIDialogAction;
import com.letion.uikit.dialog.QMUIDisplayHelper;
import com.letion.uikit.dialog.QMUIKeyboardHelper;
import com.letion.uikit.dialog.QMUIResHelper;
import com.letion.uikit.dialog.QMUIViewHelper;
import com.letion.uikit.wheelview.LoopView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liu-feng on 2017/10/9.
 */
public class HomeFragment extends Fragment {

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] listItems = new String[]{
                "消息类型对话框（蓝色按钮）",
                "消息类型对话框（红色按钮）",
                "菜单类型对话框",
                "带 Checkbox 的消息确认框",
                "单选菜单类型对话框",
                "多选菜单类型对话框",
                "带输入框的对话框",
                "高度适应键盘升降的对话框",
                "BottomSheet List",
                "BottomSheet Grid"
        };
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        mListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_list_item_1, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMessagePositiveDialog();
                        break;
                    case 1:
                        showMessageNegativeDialog();
                        break;
                    case 2:
                        showMenuDialog();
                        break;
                    case 3:
                        showConfirmMessageDialog();
                        break;
                    case 4:
                        showSingleChoiceDialog();
                        break;
                    case 5:
                        showMultiChoiceDialog();
                        break;
                    case 6:
                        showEditTextDialog();
                        break;
                    case 7:
                        showAutoDialog();
                        break;
                    case 8:
                        showSimpleBottomSheetList();
                        break;
                    case 9:
                        showSimpleBottomSheetGrid();
                        break;
                }
            }
        });
    }

    // ================================ 生成不同类型的对话框
    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("确定要发送吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showMessageNegativeDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction
                        .ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showConfirmMessageDialog() {
        new QMUIDialog.CheckBoxMessageDialogBuilder(getActivity())
                .setTitle("退出后是否删除账号信息?")
                .setMessage("删除账号信息").setChecked(true)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("退出", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showMenuDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        new QMUIDialog.MenuDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                        final BottomSheetDialog dialog1 = new BottomSheetDialog(getActivity());
                        View dialogView = LayoutInflater.from(getActivity())
                                .inflate(R.layout.layout_wheelview, null);
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < 15; i++) {
                            list.add("item " + i);
                        }
                        final LoopView loopView = (LoopView) dialogView.findViewById(R.id.loopView);
                        loopView.setItems(list);
                        dialog1.setContentView(dialogView);
                        dialog1.show();
                    }
                })
                .show();
    }

    private void showSingleChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showMultiChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3", "选项4", "选项5", "选项6"};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog
                .MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result = "你选择了 ";
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder
                (getActivity());
        builder.setTitle("标题")
                .setPlaceholder("在此输入您的昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private void showAutoDialog() {
        QMAutoTestDialogBuilder autoTestDialogBuilder = (QMAutoTestDialogBuilder) new
                QMAutoTestDialogBuilder(getActivity())
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "你点了确定", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        autoTestDialogBuilder.show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(), true);
    }

    class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private EditText mEditText;

        public QMAutoTestDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            mEditText = new EditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable
                    (mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint("输入框");
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText("观察聚焦输入框后，键盘升起降下时 dialog 的高度自适应变化。\n\n" +
                    "QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，" +
                    "同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。" +
                    "不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。");
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color
                    .qmui_config_color_gray_5));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);
            return layout;
        }
    }

    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet
                .BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.ic_launcher, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet
                .BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_launcher, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet
                        .BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_launcher, "分享到微博", TAG_SHARE_WEIBO, QMUIBottomSheet
                        .BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_launcher, "分享到私信", TAG_SHARE_CHAT, QMUIBottomSheet
                        .BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_launcher, "保存到本地", TAG_SHARE_LOCAL, QMUIBottomSheet
                        .BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener((dialog, itemView) -> {
                    dialog.dismiss();
                    int tag = (int) itemView.getTag();
                    switch (tag) {
                        case TAG_SHARE_WECHAT_FRIEND:
                            Toast.makeText(getActivity(), "分享到微信", Toast.LENGTH_SHORT).show();
                            break;
                        case TAG_SHARE_WECHAT_MOMENT:
                            Toast.makeText(getActivity(), "分享到朋友圈", Toast.LENGTH_SHORT).show();
                            break;
                        case TAG_SHARE_WEIBO:
                            Toast.makeText(getActivity(), "分享到微博", Toast.LENGTH_SHORT).show();
                            break;
                        case TAG_SHARE_CHAT:
                            Toast.makeText(getActivity(), "分享到私信", Toast.LENGTH_SHORT).show();
                            break;
                        case TAG_SHARE_LOCAL:
                            Toast.makeText(getActivity(), "保存到本地", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }).build().show();
    }

    private void showSimpleBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .setTitle("修改头像")
                .addItem("Item 1")
                .addItem(0, "Item 2", "2", true, true)
                .addItem(0, "Item 3", "3", true, false)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Item " + (position + 1), Toast.LENGTH_SHORT)
                            .show();
                })
                .build()
                .show();
    }
}
