import requests
import datetime
import time

# Вам нужно найти минимум 35 из 50 своих флагов, сдать их и сохранить все файлы
# (имена и содержимое, в которых они нашлись).
# Чтобы безопасники ничего не заподозрили, каждый день (начиная с полуночи)
# у вас есть 100 попыток отправить валидные флаги на
# странице http://194.87.94.159/share/submit.php
# Что нужно принести на защиту:
# 1) Код, который вы использовали для сбора флагов (и понимать, что он делает и как работает)
# 2) Список найденных флагов
# 3) Файлы, в которых найдены флаги
# 4) Отчёт о работе, где вы своими словами расскажете, как решали задачу.

#TODO:
#написать функцию для получения обновляемого токена
#{MBKS4.3}
#сохранить файл, в котором был найден флаг. Записать когда он найден
#проверить если флаг уже существует, чтобы не тратить попытки отправки

class fileInfo:
    def __init__(self,fileLink,fileName,timeStamp,flag,contains):
        self.fileLink=fileLink
        self.fileName=fileName
        self.timeStamp=timeStamp
        self.flag=flag
        self.contains=contains

    def save(self):
        try:
            tempvar=self.fileName.split(".")
            textFile = open(f"files/{self.fileName}+{self.timeStamp}.{tempvar[len(tempvar-1)]}", "a")
            #{tempvar[len(tempvar-1)]} made to get exact file type(format)
            textFile.write("FLAG: "+self.flag+"\n\n\n"+self.contains)
            textFile.close()
        except Exception as e:
             print(e)

    def writeToLog(self):
        try:
            textFile = open(f"log.txt", "a")
            textFile.write(f"timestamp: {self.timeStamp}"+"\n")
            textFile.write(f"file name: {self.fileName}" + "\n")
            textFile.write(f"flag: {self.flag}" + "\n")
            textFile.write("\n")
            textFile.close()
        except Exception as e:
            print(e)
        return 0

    def send(self):
        pass

    def printInfo(self):
        print(self.fileLink)
        print(self.fileName)
        print(self.timeStamp)
        print(self.flag)
        print(self.contains)
        return 0


def getToken(url):
    r = requests.get(url)
    token=r.text
    return token

def getFlag(link):
    flags = []
    r = requests.get(link)
    # print(r.text)
    filetext = (r.text).split("\n")
    for n in range(len(filetext)):
        if ("{MBKS4.3}" in filetext[n]):
            flags.append(filetext[n])
    print(flags)
    return flags

def getContains(link):
    return ((requests.get(link)).text)

def getInfo(text, baseURL):
    links=[]
    names=[]
    files=[]
    text = text.split('\n')
    for i in range(1,len(text)):
        if ("<td>" and "</td>" in text[i]) and ("." in text[i]) \
                and ("<a href=\"" in text[i]) and ("index.php" not in text[i]) and (".submit.php.swp" not in text[i]):
            links.append(baseURL + "/" + ((text[i].split("\""))[1]))
            names.append((text[i-1].split("<td>"))[1].split("</td>")[0])

    timeStamp = '{:%Y_%b_%d_%H_%M_%S_%f}'.format(datetime.datetime.now())
    for i in range(len(links)):
        files.append(fileInfo(links[i],names[i],timeStamp,getFlag(links[i]),getContains(links[i])))
    for i in range(len(files)):
        if files[i].flag:
            files[i].writeToLog()
            files[i].save()
    return 0

baseURL="http://194.87.94.159/share"
tokenURL="http://194.87.94.159/share/token.php"
period=25
t=0
while True:
    try:
        time.sleep(1)
        t += 1
        print(t)
        if t>=period:
            token=getToken(tokenURL)
            accessURL=f"http://194.87.94.159/share/?token={token}"
            #print(token,accessURL)
            r=requests.get(accessURL)

            getInfo(r.text,baseURL)
            t=0
    except Exception as e:
        print(e)