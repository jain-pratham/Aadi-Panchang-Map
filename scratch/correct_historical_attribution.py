import os

repo_root = r"c:\Projects\Freelancer\Sky Map_Original\stardroid-v1"
app_res = os.path.join(repo_root, "app", "src", "main", "res")

english_new_sentence = '&lt;p&gt;The original Sky Map project was developed by Google Inc. and open sourced in 2012. Aadi Panchang Map is an independent derivative project based on that open-source code.&lt;/p&gt;'

# Map of localized updated sentences preserving exact localization language
localized_sentences = {
    "values": english_new_sentence,
    "values-b+en+GB": english_new_sentence,
    "values-hi": '&lt;p&gt;मूल Sky Map प्रोजेक्ट Google Inc. द्वारा विकसित किया गया था और 2012 में ओपन सोर्स किया गया था। Aadi Panchang Map उस ओपन-सोर्स कोड पर आधारित एक स्वतंत्र डेरिवेटिव प्रोजेक्ट है।&lt;/p&gt;',
    "values-de": '&lt;p&gt;Das ursprüngliche Sky Map-Projekt wurde von Google Inc. entwickelt und 2012 als Open Source veröffentlicht. Aadi Panchang Map ist ein unabhängiges abgeleitetes Projekt, das auf diesem Open-Source-Code basiert.&lt;/p&gt;',
    "values-es": '&lt;p&gt;El proyecto original Sky Map fue desarrollado por Google Inc. y se lanzó como código abierto en 2012. Aadi Panchang Map es un proyecto derivado independiente basado en ese código de código abierto.&lt;/p&gt;',
    "values-fr": '&lt;p&gt;Le projet original Sky Map a été développé par Google Inc. et publié en open source en 2012. Aadi Panchang Map est un projet dérivé indépendant basé sur ce code open source.&lt;/p&gt;',
    "values-it": '&lt;p&gt;Il progetto originale Sky Map è stato sviluppato da Google Inc. e reso open source nel 2012. Aadi Panchang Map è un progetto derivado indipendente basato su quel codice open source.&lt;/p&gt;',
    "values-ja": '&lt;p&gt;オリジナルのSky MapプロジェクトはGoogle Inc.によって開発され、2012年にオープンソース化されました。Aadi Panchang Mapは、そのオープンソースコードに基づく独立した派生プロジェクトです。&lt;/p&gt;',
    "values-pt": '&lt;p&gt;O projeto original Sky Map foi desenvolvido pela Google Inc. e disponibilizado como código aberto em 2012. Aadi Panchang Map é um projeto derivado independente baseado nesse código-fonte aberto.&lt;/p&gt;',
    "values-ru": '&lt;p&gt;Оригинальный проект Sky Map был разработан компанией Google Inc. и открыт в 2012 году. Aadi Panchang Map является независимым производным проектом на основе этого открытого исходного кода.&lt;/p&gt;',
    "values-b+zh+Hans": '&lt;p&gt;原始的 Sky Map 项目由 Google Inc. 开发并于 2012 年开源。Aadi Panchang Map 是基于该开源代码的独立衍生项目。&lt;/p&gt;',
    "values-b+zh+Hant": '&lt;p&gt;原始的 Sky Map 專案由 Google Inc. 開發並於 2012 年開源。Aadi Panchang Map 是基於該開源程式碼的獨立衍生專案。&lt;/p&gt;',
}

updated_files = []

for root, dirs, files in os.walk(app_res):
    if "credits.xml" in files:
        fp = os.path.join(root, "credits.xml")
        folder_name = os.path.basename(root)
        rel_p = os.path.relpath(fp, repo_root)
        
        with open(fp, "r", encoding="utf-8", errors="ignore") as file:
            lines = file.readlines()
        
        target_sentence = localized_sentences.get(folder_name, english_new_sentence)
        modified = False
        new_lines = []
        
        for line in lines:
            if "2012" in line and ("Google" in line or "Sky Map" in line or "Aadi Panchang Map" in line or "2012" in line):
                new_lines.append(target_sentence + "\n")
                modified = True
            else:
                new_lines.append(line)
        
        if modified:
            with open(fp, "w", encoding="utf-8") as file:
                file.writelines(new_lines)
            updated_files.append(rel_p)
            print(f"CORRECTED CREDITS [{folder_name}]: {rel_p}")

print(f"\nTotal credits.xml files updated: {len(updated_files)}")

