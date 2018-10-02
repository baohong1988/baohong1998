import numpy as np
import os
import cv2


filename = 'video.avi'
frames_per_second = 40.0
res = '720p'
def make_1080p():
    cap.set(3, 1920)
    cap.set(4, 1080)

def make_720p():
    cap.set(3, 1280)
    cap.set(4, 720)

def make_480p():
    cap.set(3, 640)
    cap.set(4, 480)

def change_res(cap, width, height):
    cap.set(3, width)
    cap.set(4, height)


def rescale_frame(frame, percent=75):
    width = int(frame.shape[1] * percent/ 100)
    height = int(frame.shape[0] * percent/ 100)
    dim = (width, height)
    return cv2.resize(frame, dim, interpolation =cv2.INTER_AREA)

# Standard Video Dimensions Sizes
STD_DIMENSIONS =  {
    "480p": (640, 480),
    "720p": (1280, 720),
    "1080p": (1920, 1080),
    "4k": (3840, 2160),
}
def get_dims(cap, res='1080p'):
    width, height = STD_DIMENSIONS["480p"]
    if res in STD_DIMENSIONS:
        width,height = STD_DIMENSIONS[res]
    ## change the current caputre device
    ## to the resulting resolution
    change_res(cap, width, height)
    return width, height

VIDEO_TYPE = {
    'avi': cv2.VideoWriter_fourcc(*'XVID'),
    #'mp4': cv2.VideoWriter_fourcc(*'H264'),
    'mp4': cv2.VideoWriter_fourcc(*'XVID'),
}
def get_video_type(filename):
    filename, ext = os.path.splitext(filename)
    if ext in VIDEO_TYPE:
      return  VIDEO_TYPE[ext]
    return VIDEO_TYPE['avi']

cap = cv2.VideoCapture(0)
dim = get_dims(cap, res=res)
out = cv2.VideoWriter(filename, get_video_type(filename), frames_per_second, dim)

while True:
	ret, frame = cap.read()
	frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	out.write(frame)
	'''frame75 = rescale_frame(frame, percent=75)
	frame150 = rescale_frame(frame, percent=150)
	frame60 = rescale_frame(frame, percent=60)
	frame30 = rescale_frame(frame, percent=30)
	frame200 = rescale_frame(frame, percent=200)'''
	cv2.imshow('frame', frame)
	'''cv2.imshow('frame75', frame75)
	cv2.imshow('frame150', frame150)
	cv2.imshow('frame60', frame60)
	cv2.imshow('frame30', frame30)
	cv2.imshow('frame200', frame200)'''

	if cv2.waitKey(20) & 0xFF == ord('q'):
		break
	
cap.release()
out.release()
cv2.destroyAllWindows()