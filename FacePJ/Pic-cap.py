import cv2
import time
import os



BASE_DIR = os.path.dirname(os.path.abspath(__file__))
direct = os.path.join(BASE_DIR, "images")
num_files = 0
os.chdir(direct)

name = input("What is your name?\n")
type(name)
cur = direct + "\{}".format(name)

print(cur)
if(os.path.isdir(cur)):
	l = os.listdir(cur)
	num_files = len(l)
	
else:
	os.mkdir(name)

i=0
while True:
	
	print("In ")
	for x in range(3):
		print("%d...\n" %(x+1))
		time.sleep(1)
	
	cap = cv2.VideoCapture(0)
	ret, frame = cap.read()
	cv2.imshow('frame%d' %i , frame)
	cv2.imwrite("{}/frame{}.jpg".format(name, num_files), frame)
	i = i+1
	num_files = num_files+1
	cap.release()
	if(i == 10 or num_files >= 10):
		break
	if cv2.waitKey(20) & 0xFF == ord('q'):
		break
	




cv2.destroyAllWindows()