#!/usr/bin/python

# Import libraries
import sys
import os

def parse(inputFile):
    # put all csv column in a dict
    file = None
    try :
        file = open(inputFile, "r")
    except :
        file = open(os.path.join(os.getcwd(),inputFile),"r")
    finally:
        if (file == None) :
            print "File not found !!! "
            return
    # parse all cvs file
    l__title = file.readline().split(";")
    l__filename = file.readline().split(";")
    l__date = file.readline().split(";")
    l__mission = file.readline().split(";")
    l__techno = file.readline().split(";")
    l__ressource = file.readline().split(";")
    l__type = file.readline().split(";")
    l__image = file.readline().split(";")
    l__shortDesc = file.readline().split(";")
    l__longDesc = file.readline().split(";")
    l__cost = file.readline().split(";")
    l__PV = file.readline().split(";")
    l__valeur = file.readline().split(";")
    l__ressources = file.readline().split(";")
    l__moreActions = file.readline().split(";")
    l__moreCards = file.readline().split(";")
    l__moreCoins = file.readline().split(";")
    l__revealTop = file.readline().split(";")
    l__discardDeck = file.readline().split(";")
    l__lotsActions = file.readline().split(";")
    l__sevenDraw = file.readline().split(";")
    l__batchVP = file.readline().split(";")
    
    # create all files
    for i in range(1,2000) :
        if len(l__title) <= i :
            break
        if (l__title[i] == None) or (l__title[i].strip() == "" ) :
            continue
        
        xml_file = """ <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<card type="%s" cost="%s" date="%s">
    <title>"%s"</title>
    <shortDescription>"%s"</shortDescription>
    <longDescription>"%s"</longDescription>
    <filename>"%s"</filename>
    <victoryPoints>"%s"</victoryPoints>
    <moreActions count="%s">
    <moreCards count="%s">
    <moreCoins count="%s">
    <revealTop count="%s">
    <discardDeck count="%s">
    <lotsActions count="%s">
    <sevenDraw count="%s">
    <batchVP count="%s">
</card>
        """%(l__type[i],l__cost[i],l__date[i],l__title[i],l__shortDesc[i],l__longDesc[i],
             l__filename[i],l__PV[i], l__moreActions[i],l__moreCards[i], l__moreCoins[i],
             l__revealTop[i], l__discardDeck[i], l__lotsActions[i], l__sevenDraw[i],
             l__batchVP[i])
        
        dirname = os.path.join(os.getcwd(),l__filename[i])
        if not os.path.isdir(dirname) :
            os.mkdir(dirname)
        filename = os.path.join(dirname, l__filename[i]+".xml")

        file = open(filename, "w")
        # create xml file
        file.write(xml_file)
        file.close()

if (__name__ == '__main__'):
    if len(sys.argv) > 1 :
        parse(sys.argv[1])
    else :
        print "should be call with a .csv file. Ex card-cvs2xml.py file.cvs"