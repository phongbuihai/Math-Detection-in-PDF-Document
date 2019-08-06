clear, close all

%%Chuan bi trainingset va test set, chuyen het sang anh RGB training set
%%va test set
tic;
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\Resnet_Train\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
categories = {'text', 'variable'};
imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category
% Use splitEachLabel method to trim the set.
imds = splitEachLabel(imds, minSetCount);
training_total_label=imds.Labels;
%%Get training set
[trainingSet, ~] = splitEachLabel(imds, minSetCount);

%% Load Pre-trained CNN

convnet = alexnet;
imageSize = convnet.Layers(1).InputSize;

%Extract train feature
augmentedTrainingSet = augmentedImageDatastore(imageSize, trainingSet, 'ColorPreprocessing', 'gray2rgb');
layer = 'fc7';
featuresTrain = activations(convnet,augmentedTrainingSet,layer,'OutputAs','rows');

%Display training labels
YTrain = trainingSet.Labels;
%YTest = imdsTest.Labels;
%Train multi class svm using training features and labels
classifier = fitcecoc(featuresTrain,YTrain);
%Classification test images
%YPred = predict(classifier,featuresTest);


%% Evaluate Classifier


% Pass CNN image features to trained classifier
%Doc anh va phan loai anh nho SVM da duoc train
files =dir( fullfile(testFolder, '*.*') );%Doc cac file trong folder
allNames = { files.name };
%disp(allNames);
l=length(files);
w=45;
h=1000;
for id = 3:l
   % strcat(mathfolder,'\\');
   % filename=strcat(mathfolder,allNames(id));%get path and file name
    filename= fullfile(testFolder,allNames(id));
    filename=char(filename); %convert cell to string
    img=imread(filename);
   % img=imresize(img,[w h]);
    if ~islogical(img)
    img=im2bw(img);
    end
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    Test_Image{id-2}=img;
    File_Image{id-2}=filename;
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    ds = augmentedImageDatastore(imageSize, RGBImage, 'ColorPreprocessing', 'gray2rgb');
    %ds = augmentedImageDatastore(imageSize, newImage);
     % Extract image features using the CNN
    imageFeatures = activations(convnet, ds, layer, 'OutputAs', 'columns');
    TestFeatures{id-2}=imageFeatures;
    %featuresTrain = activations(convnet,ds,layer,'OutputAs','rows');
    % Make a prediction using the classifier
    svm_result_label{id-2}= predict(classifier, imageFeatures, 'ObservationsIn', 'columns');
end
toc;

%%[train_row,train_col]=size(trainingFeatures);

%%for i=1:train_row
  %%  for j=1:train_col
    %%   trainingFeatures_transform (j,i)= featuresTrain(i,j);
    %%end
%%end

%Mdl = fitcknn(trainingFeatures_transform, trainingLabels);
%predict(Mdl,transpose(imageFeatures))

Mdl = fitcknn(featuresTrain, YTrain);

TestFeatures=cell2mat(TestFeatures);

[test_row,test_col]=size(TestFeatures);

for i=1:test_row
    for j=1:test_col
       TestFeatures_transform (j,i)= TestFeatures(i,j);
    end
end

kNN_Result=predict(Mdl,TestFeatures_transform);


%%%Su dung tang cuong du lieu xoay



tic;
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Train_28_1\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';

%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\TiengViet\Train\';
%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\Train_English_9_7\train\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\TiengViet\testset\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\testset\';
categories = {'text', 'variable'};
imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category
% Use splitEachLabel method to trim the set.
imds = splitEachLabel(imds, minSetCount);
training_total_label=imds.Labels;
%%Get training set
[trainingSet, ~] = splitEachLabel(imds, minSetCount);
imageSize = [40 120 3];
imageAugmenter = imageDataAugmenter( ...
    'RandXTranslation',[0 0.5], ...
    'RandYTranslation',[0 0.5]);
% Apply transformations (using randmly picked values) and build augmented
% data store
augImds = augmentedImageDatastore(imageSize,imds,'DataAugmentation',imageAugmenter);
%% Create training and validation sets
[augImdsTrainingSet, augImdsValidationSet] = splitEachLabel(imds, 0.7, 'randomize');
    
%% Train the network. 
net2 = trainNetwork(augImdsTrainingSet,layers,options);
%% Report accuracy of baseline classifier with image data augmentation
YPred = classify(net2,augImdsValidationSet);
YValidation = augImdsValidationSet.Labels;
augImdsAccuracy = sum(YPred == YValidation)/numel(YValidation);



tic;
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\Resnet_Train\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';

%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Train_MAPR\';
%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\Train_English_9_7\train\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\TiengViet\testset\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test_MAPR\';
categories = {'text', 'variable'};

imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
imageAugmenter = imageDataAugmenter( ...
    'RandRotation',[-20,20], ...
    'RandXTranslation',[-5 5], ...
    'RandYTranslation',[-5 5]);
% Apply transformations (using randmly picked values) and build augmented
% data store
imageSize = [40 40 3];
augImds = augmentedImageDatastore(imageSize,imds,'DataAugmentation',imageAugmenter);
[augmentedTrainingSet, augImdsValidationSet] = splitEachLabel(imds, 1.0, 'randomize');

tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category
% Use splitEachLabel method to trim the set.
imds = splitEachLabel(imds, minSetCount);
training_total_label=imds.Labels;
%%Get training set
[trainingSet, ~] = splitEachLabel(imds, minSetCount);

%% Load Pre-trained CNN

convnet = alexnet;
imageSize = convnet.Layers(1).InputSize;

%Extract train feature
augmentedTrainingSet = augmentedImageDatastore(imageSize, trainingSet, 'ColorPreprocessing', 'gray2rgb');
layer = 'fc7';
featuresTrain = activations(convnet,augmentedTrainingSet,layer,'OutputAs','rows');

%Display training labels
YTrain = trainingSet.Labels;
%YTest = imdsTest.Labels;
%Train multi class svm using training features and labels
classifier = fitcecoc(featuresTrain,YTrain);
%Classification test images
%YPred = predict(classifier,featuresTest);


%% Evaluate Classifier


% Pass CNN image features to trained classifier
%Doc anh va phan loai anh nho SVM da duoc train
files =dir( fullfile(testFolder, '*.*') );%Doc cac file trong folder
allNames = { files.name };
%disp(allNames);
l=length(files);
w=45;
h=1000;
for id = 3:l
   % strcat(mathfolder,'\\');
   % filename=strcat(mathfolder,allNames(id));%get path and file name
    filename= fullfile(testFolder,allNames(id));
    filename=char(filename); %convert cell to string
    img=imread(filename);
   % img=imresize(img,[w h]);
    if ~islogical(img)
    img=im2bw(img);
    end
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    Test_Image{id-2}=img;
    File_Image{id-2}=filename;
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    ds = augmentedImageDatastore(imageSize, RGBImage, 'ColorPreprocessing', 'gray2rgb');
    %ds = augmentedImageDatastore(imageSize, newImage);
     % Extract image features using the CNN
    imageFeatures = activations(convnet, ds, layer, 'OutputAs', 'columns');
    TestFeatures{id-2}=imageFeatures;
    %featuresTrain = activations(convnet,ds,layer,'OutputAs','rows');
    % Make a prediction using the classifier
    svm_result_label{id-2}= predict(classifier, imageFeatures, 'ObservationsIn', 'columns');
end
toc;








tic;
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Train_28_1\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';

%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\TiengViet\Train\';
%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\Train_English_9_7\train\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\TiengViet\testset\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\testset\';
categories = {'text', 'variable'};
imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category
% Use splitEachLabel method to trim the set.
imds = splitEachLabel(imds, minSetCount);
training_total_label=imds.Labels;
%%Get training set
[trainingSet, ~] = splitEachLabel(imds, minSetCount);
imageSize = [224 224 3];
imageAugmenter = imageDataAugmenter( ...
    'RandXTranslation',[0 0.0], ...
    'RandYTranslation',[0 0.0]);
% Apply transformations (using randmly picked values) and build augmented
% data store
augImds = augmentedImageDatastore(imageSize,imds,'DataAugmentation',imageAugmenter);
%augImds = augmentedImageDatastore(imds,'DataAugmentation');

convnet = alexnet;
imageSize = convnet.Layers(1).InputSize;

%Extract train feature
augmentedTrainingSet = augmentedImageDatastore(imageSize, imds, 'ColorPreprocessing', 'gray2rgb');


dnds = denoisingImageDatastore(imds,...
    'PatchesPerImage',512,...
    'PatchSize',50,...
    'GaussianNoiseLevel',[0.01 0.1],...
    'ChannelFormat','grayscale');
