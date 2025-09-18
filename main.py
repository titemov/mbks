import requests
import datetime
import time
from logging import getLogger, StreamHandler, FileHandler, Formatter

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
            textFile = open(f"{self.fileName}_{self.timeStamp}.{tempvar[len(tempvar)-1]}", "a")
            #{tempvar[len(tempvar-1)]} made to get exact file type(format)
            for i in range(len(self.flag)):
                textFile.write("FLAG: " + self.flag[i]+"\n")
            textFile.write("\n\n\n"+self.contains)
            textFile.close()
        except Exception as e:
             print(e)

    def writeToLog(self):
        try:
            textFile = open(f"log.txt", "a")
            textFile.write(f"timestamp: {self.timeStamp}"+"\n")
            textFile.write(f"file name: {self.fileName}" + "\n")
            for i in range(len(self.flag)):
                textFile.write(f"flag: {self.flag[i]}" + "\n")
            textFile.write("\n")
            textFile.close()
        except Exception as e:
            print(e)
        return 0

    def send(self):
        for i in range(len(self.flag)):
            requests.post(submitURL, data={
                "brigade": "4.3",
                "flag": self.flag[i]
            })
        return 0

    def printInfo(self):
        print(self.fileLink)
        print(self.fileName)
        print(self.timeStamp)
        print(self.flag)
        print(" ")
        return 0


def getToken(url):
    r = requests.get(url)
    token=r.text
    return token

def getFlag(link):
    try:
        flags = []
        r = requests.get(link)
        # print(r.text)
        filetext = (r.text).split("\n")
        for n in range(len(filetext)):
            if ("{MBKS4.3}" in filetext[n]):
                flags.append(filetext[n])
        #print(flags)
        return flags
    except Exception as e:
        print(e)
        return -1

def getContains(link):
    return ((requests.get(link)).text)

def getInfo(text, baseURL):
    files=[]
    text = text.split('\n')
    timeStamp = '{:%Y_%b_%d_%H_%M_%S_%f}'.format(datetime.datetime.now())
    for i in range(1,len(text)):
        if ("<td>" and "</td>" in text[i]) and ("." in text[i]) \
                and ("<a href=\"" in text[i]) and ("index.php" not in text[i]) and (".submit.php.swp" not in text[i]):
            link=baseURL + "/" + ((text[i].split("\""))[1])
            name=(text[i-1].split("<td>"))[1].split("</td>")[0]
            flags=getFlag(link)
            if flags==-1: flags=[]
            files.append(fileInfo(link,name,timeStamp,flags,getContains(link)))

    return files

def analyzeInfo(files):
    #if file have flag
    #   try to open file as read
    #   error? create a file and write flag(s) in it
    #   no error? check if flag is in file
    for i in range(len(files)):
        files[i].printInfo()
        if files[i].flag:
            files[i].writeToLog()
            try:
                f = open("flags.txt", "r")
                flags = [str(i) for i in f]
                if not flags: raise OSError #(!)
                f.close()
                for n in range(len(files[i].flag)):
                    alreadyFound = False
                    for m in range(len(flags)):
                        if (files[i].flag)[n] in flags[m]:
                            alreadyFound = True
                    if not alreadyFound:
                        textFile = open("flags.txt", "a")
                        for m in range(len(files[i].flag)):
                            textFile.write((files[i].flag)[m]+"\n")
                        textFile.close()
                        files[i].send()
                        files[i].save()
            except OSError as e:
                print(e)
                f = open("flags.txt", "w")
                for n in range(len(files[i].flag)):
                    f.write((files[i].flag)[n]+"\n")
                files[i].send()
                files[i].save()
                f.close()
    return 0


baseURL="http://194.87.94.159/share"
tokenURL="http://194.87.94.159/share/token.php"
submitURL="http://194.87.94.159/share/submit.php"
period=25
t=0
while True:
    try:
        time.sleep(1)
        t += 1
        print(t)
        if t>=period:
            accessURL=f"http://194.87.94.159/share/?token={getToken(tokenURL)}"
            r=requests.get(accessURL)

            analyzeInfo(getInfo(r.text,baseURL))

            t=0
    except Exception as e:
        print(e)