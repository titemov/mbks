# Максим устроился в корпорацию SuperSec, которая разрабатывает системы документооборота на основе
# случайных символов. Он был уверен, что первым делом его отправят взламывать конкурентов с помощью nmap,
# но посадили разбираться с формальными моделями безопасности компьютерных систем.
#
# К концу первого рабочего дня Максим выяснил, что формальные модели это обязательная основа знаний
# безопасника, а доступы к файлам в SuperSec разграничиваются с помощью матрицы доступа.
# Через API можно или получить всю матрицу сразу (http://194.87.94.159/supersec/api.php?action=get),
# или изменить доступ к определённому файлу для определённого пользователя
# на эндпоинте http://194.87.94.159/supersec/api.php, передав action=set и,
# указав аргументы user, file и value (+ или -).
#
# Известно, что пару недель назад из корпорации уволился один редактор, из-за которого безопасники
# теперь спят по два часа в день. Поэтому завтра утром Максим получит от коллег из отдела случайного
# документооборота актуальный список правильных доступов, примерно такой (но размер списка заранее неизвестен):
#
# oscar21 Strategy_2025.docx -
# eve martinez Marketing_Plan.pptx +
# ivan3 Audit_Report.pdf +
# bob ivanovich dylan Audit_Report.pdf -
#
# Каждая строка содержит одно имя, один файл и один квалификатор доступа
#
# Нужно будет настроить доступы и найти нарушителей. Времени мало, вы должны помочь Максиму.
#
# Задача: написать пользовательскую программу с понятным способом взаимодействия, которая проверит,
# действительно ли выданные доступы из списка установлены в матрице. Если доступы не совпадают,
# программа должна автоматически их изменить на правильные и выдать список изменений.
#
# При этом если в списке «правильных» доступов есть пользователи или файлы, которых в матрице нет,
# программа должна сообщить об этом и предложить пользователю принять решение по дальнейшим действиям.
#
# Кроме этого, предусмотрите возможности:
# 1) Наглядного просмотра матрицы целиком
# 2) Списка активных доступов для конкретного указанного пользователя.
# 3) Выдачи прав доступа на заданный файл заданному пользователю в ручном режиме.

import requests
import datetime
import time
import json

#TODO:
#1)+Сделать класс worker, содержащий информацию о имени пользователя, и словарь доступа к файлам вида {"+": [], "-":[]}
#2)+Сделать парсер txt файла, где имя может содержать пробелы, имя файла не может. Каждый аргумент разделен пробелом
#3) Сделать функцию сравнения и выбора дальнейших действий:
#   сравнить с полученным списком:
#       если нет указанного в списке пользователя или файла нет в матрице -> предложить выбор (добавить/игнор ?)
#       если неверно указан доступ к существующим файлам -> исправить полученный список, записать в changelog исправления.
#           Вернуть список после всех исправлений и вернуть содержимое changelog (каждый запуск - новый changelog)
#4) Написать функцию, которая печатает красивую ТАБЛИЦУ (матрицу) доступа: столбцы - имена файлов, строки - имена пользователей
#
class worker:
    def __init__(self, name):
        self.name=name
        self.access={"+": [], "-": []}

    def printInfo(self):
        print("User name: ", self.name)
        print("Access: ", self.access)
        print(" ")
        return 0

def readCorrectList(path):
    correctListWorkers = []
    correctList = []
    try:
        f = open(path,"r")
        correctList=[str(i) for i in f]
        f.close()
    except OSError as e:
        print(e)
        return 1

    if not correctList: return 2

    print(correctList)

    for i in range(len(correctList)):
        userNames=[]
        temp=correctList[i].split(" ")
        print(temp)
        value = ((temp[(len(temp) - 1) - 0]).split("\n"))[0]
        file = temp[(len(temp) - 1) - 1]
        name=""
        for n in range(len(temp)-2):
            if n==(len(temp)-2):
                name+=temp[n]
            else:
                name+=(temp[n]+" ")

        if name not in userNames:
            userNames.append(name)
            tempObj=worker(name)
            if value=="+":
                tempObj.access["+"].append(file)
            else:
                tempObj.access["-"].append(file)
            correctListWorkers.append(tempObj)
        else:
            for m in range(len(correctListWorkers)):
                if correctListWorkers[m]==name:
                    if value=="+":
                        correctListWorkers[m].access["+"].append(file)
                    else:
                        correctListWorkers[m].access["-"].append(file)

    return correctListWorkers

def initWorker(data):
    workers=[]
    userNames=[]

    for file in data:
        for name in data[file]:
            if name not in userNames:
                userNames.append(name)
                tempObj = worker(name)
                if data[file][name]=="+":
                    (tempObj.access)["+"].append(file)
                else:
                    (tempObj.access)["-"].append(file)
                workers.append(tempObj)
            else:
                for i in range(len(workers)):
                    if workers[i].name==name:
                        if data[file][name]=="+":
                            (workers[i].access)["+"].append(file)
                        else:
                            (workers[i].access)["-"].append(file)
    return workers

def compareWorkers(correctListWorkers, workers):

    return 0

matrixURL="http://194.87.94.159/supersec/api.php?action=get"

def getJSON(url):
    return json.loads(requests.get(url).text)

#print(getJSON(matrixURL))
data=getJSON(matrixURL)

# temp=initWorker(data)
# for i in range(len(temp)):
#     temp[i].printInfo()

a=readCorrectList("test.txt")

for i in range(len(a)):
    a[i].printInfo()

# print(json.loads(requests.get(matrixURL).text)["IT_Infrastructure.xlsx"])
# print(json.loads(requests.get(matrixURL).text)["IT_Infrastructure.xlsx"]["alice01"]) # - value