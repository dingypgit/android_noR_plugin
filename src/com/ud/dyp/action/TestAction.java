package com.ud.dyp.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.ud.dyp.dialog.MainDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ud-dyp on 2017/8/19.
 */
public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

//        PsiClass psiClass = getPsiClassFromContext(e);
//        String text = psiClass.getText();
//        System.out.println(text);

        changeSelectText(e,"");


    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {

        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);

        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    private void changeSelectText(AnActionEvent e,String text) {
        Project mProject = e.getData(PlatformDataKeys.PROJECT);
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        Document document = mEditor.getDocument();

        System.out.println(document.getText());

        String regEx = "R\\.[a-z]*\\.\\w*";
        List<String> idList = getRxList(document.getText(),regEx);

        String regExType = "extends\\s\\w*";
        List<String> clssTypeList = getRxList(document.getText(),regExType);

        String ssReplaced = getRelaceedStr(document.getText(),idList,clssTypeList);

        final String ssF = ssReplaced;
        final int start = 0;
        final int end = document.getText().length();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.replaceString(start, end, ssF);
            }
        };
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }

    private static String replaceResource(String idStr,String type){
        String[] split = idStr.split("\\.");
        String sourceFormat = null;
        if (type.contains("Fragment")){
            sourceFormat = new String("getResources().getIdentifier(\"%s\",\"%s\",getActivity().getPackageName())");
        }else {
            sourceFormat = new String("getResources().getIdentifier(\"%s\",\"%s\",getPackageName())");
        }
        return String.format(sourceFormat,split[2],split[1]);
    }

    private List<String> getRxList(String data,String regEx){
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(data);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
    private String getRelaceedStr(String data,List<String> list,List<String> typeList){
        String ssReplaced = data;
        String type = "Activity";
        if (typeList!=null && typeList.size()>0){
            type = typeList.get(0);
        }
        for (String item : list) {
            String s = replaceResource(item,type);
            ssReplaced = ssReplaced.replaceAll(item,s);
        }
        return ssReplaced;
    }

}
