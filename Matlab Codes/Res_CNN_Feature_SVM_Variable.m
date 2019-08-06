%%Chu y chuyen tu anh nhi phan sang RGB truoc khi su dung CNN
%%Khong can resize anh
tic;
%trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\Resnet_Train\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
%rootFolder = fullfile(outputFolder, '101_ObjectCategories');
%categories = {'airplanes', 'ferry', 'laptop'};
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\Resnet_Train\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
categories = {'text', 'variable'};
%for i=1:29
 %   categories{i}=strcat('letter',num2str(i));
%end
%lop= {'C_141', 'conlai'};
%categories = {'1_a', '3_a', '8_a', '16_a', '16_c', '18_a', '18_e','20_a', '20_b', '20_c', '21_a', '24_a', '25_a', '26_a', '27_a', '27_b', '28_a', '29_a', '29_b', '29_c', '30_27_d', '32_a','33'};
imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category

% Use splitEachLabel method to trim the set.
%imds = splitEachLabel(imds, minSetCount, 'randomize');
imds = splitEachLabel(imds, minSetCount);
% Notice that each set now has exactly the same number of images.
countEachLabel(imds)
% Find the first instance of an image for each category
%text = find(imds.Labels == 'text', 1);
%variable = find(imds.Labels == 'variable', 1);

% Load pretrained network
net = resnet50();
%net=alexnet;
net.Layers(1)
net.Layers(end);
numel(net.Layers(end).ClassNames);
%[trainingSet, ~] = splitEachLabel(imds, 1, 'randomize');
[trainingSet, ~] = splitEachLabel(imds, minSetCount);
% Create augmentedImageDatastore from training and test sets to resize
% images in imds to the size required by the network.
imageSize = net.Layers(1).InputSize;
augmentedTrainingSet = augmentedImageDatastore(imageSize, trainingSet, 'ColorPreprocessing', 'gray2rgb');
%augmentedTestSet = augmentedImageDatastore(imageSize, testSet, 'ColorPreprocessing', 'gray2rgb');

% Get the network weights for the second convolutional layer
w1 = net.Layers(2).Weights;

% Scale and resize the weights for visualization
w1 = mat2gray(w1);
w1 = imresize(w1,5);

%Get feature of training set
featureLayer = 'fc1000';
trainingFeatures = activations(net, augmentedTrainingSet, featureLayer, ...
    'MiniBatchSize', 32, 'OutputAs', 'columns');

% Get training labels from the trainingSet
trainingLabels = trainingSet.Labels;

% Train multiclass SVM classifier using a fast linear solver, and set
% 'ObservationsIn' to 'columns' to match the arrangement used for training
% features.
classifier = fitcecoc(trainingFeatures, trainingLabels, ...
    'Learners', 'Linear', 'Coding', 'onevsall', 'ObservationsIn', 'columns');


%newImage=imread('C:\Users\Admin\AppData\Local\Temp\caltech101\101_ObjectCategories\airplanes\image_0353.jpg');
%ds = augmentedImageDatastore(imageSize, newImage, 'ColorPreprocessing', 'gray2rgb');

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
    if ~islogical(img)
    img=im2bw(img);
    end
    Test_Image{id-2}=img;
    File_Image{id-2}=filename;
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    ds = augmentedImageDatastore(imageSize, RGBImage, 'ColorPreprocessing', 'gray2rgb');
    %ds = augmentedImageDatastore(imageSize, newImage);
     % Extract image features using the CNN
    imageFeatures = activations(net, ds, featureLayer, 'OutputAs', 'columns');
    %% Make a prediction using the SVM classifier
    svm_result_label{id-2}= predict(classifier, imageFeatures, 'ObservationsIn', 'columns');
    TestFeatures{id-2}=imageFeatures;
    %%classify using kNN
    %kNN_Result{id-2} = knnclassify(imageFeatures, trainingFeatures, trainingLabels);
end
toc;
%%Classification using kNN model
[train_row,train_col]=size(trainingFeatures);

for i=1:train_row
    for j=1:train_col
       trainingFeatures_transform (j,i)= trainingFeatures(i,j);
    end
end

%Mdl = fitcknn(trainingFeatures_transform, trainingLabels);
%predict(Mdl,transpose(imageFeatures))

Mdl = fitcknn(trainingFeatures_transform, trainingLabels);

TestFeatures=cell2mat(TestFeatures);

[test_row,test_col]=size(TestFeatures);

for i=1:test_row
    for j=1:test_col
       TestFeatures_transform (j,i)= TestFeatures(i,j);
    end
end

kNN_Result=predict(Mdl,TestFeatures_transform);





%%Su dung ky thuat tang cuong du lieu
imageAugmenter = imageDataAugmenter( ...
    'RandRotation',[-20,20], ...
    'RandXTranslation',[-5 5], ...
    'RandYTranslation',[-5 5]);
% Apply transformations (using randmly picked values) and build augmented
% data store
augImds = augmentedImageDatastore(imageSize,imds,'DataAugmentation',imageAugmenter);
%% Create training and validation sets
[augImdsTrainingSet, augImdsValidationSet] = splitEachLabel(imds, 0.7, 'randomize');


%%Su dung tang cuong du lieu
tic;
trainFolder ='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\Resnet_Train\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
%rootFolder = fullfile(outputFolder, '101_ObjectCategories');
%categories = {'airplanes', 'ferry', 'laptop'};
%trainFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Train_MAPR\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test_MAPR\';
categories = {'text', 'variable'};
%for i=1:29
 %   categories{i}=strcat('letter',num2str(i));
%end
%lop= {'C_141', 'conlai'};
%categories = {'1_a', '3_a', '8_a', '16_a', '16_c', '18_a', '18_e','20_a', '20_b', '20_c', '21_a', '24_a', '25_a', '26_a', '27_a', '27_b', '28_a', '29_a', '29_b', '29_c', '30_27_d', '32_a','33'};
imds = imageDatastore(fullfile(trainFolder, categories), 'LabelSource', 'foldernames');
tbl = countEachLabel(imds);
minSetCount = min(tbl{:,2}); % determine the smallest amount of images in a category
%%Tang cuong du lieu
imageAugmenter = imageDataAugmenter( ...
    'RandRotation',[-20,20], ...
    'RandXTranslation',[-5 5], ...
    'RandYTranslation',[-5 5]);
% Apply transformations (using randmly picked values) and build augmented
% data store
imageSize = [224 224 3];
augImds = augmentedImageDatastore(imageSize,imds,'DataAugmentation',imageAugmenter);
[augmentedTrainingSet, augImdsValidationSet] = splitEachLabel(imds, 1.0, 'randomize');
% Use splitEachLabel method to trim the set.
%imds = splitEachLabel(imds, minSetCount, 'randomize');
imds = splitEachLabel(imds, minSetCount);
% Notice that each set now has exactly the same number of images.
countEachLabel(imds)


% Load pretrained network
net = resnet50();
%net=alexnet;
net.Layers(1)
net.Layers(end);
numel(net.Layers(end).ClassNames);
%[trainingSet, ~] = splitEachLabel(imds, 1, 'randomize');
[trainingSet, ~] = splitEachLabel(imds, minSetCount);
% Create augmentedImageDatastore from training and test sets to resize
% images in imds to the size required by the network.
imageSize = net.Layers(1).InputSize;
augmentedTrainingSet = augmentedImageDatastore(imageSize, trainingSet, 'ColorPreprocessing', 'gray2rgb');
%augmentedTestSet = augmentedImageDatastore(imageSize, testSet, 'ColorPreprocessing', 'gray2rgb');

% Get the network weights for the second convolutional layer
w1 = net.Layers(2).Weights;

% Scale and resize the weights for visualization
w1 = mat2gray(w1);
w1 = imresize(w1,5);

%Get feature of training set
featureLayer = 'fc1000';
trainingFeatures = activations(net, augmentedTrainingSet, featureLayer, ...
    'MiniBatchSize', 32, 'OutputAs', 'columns');

% Get training labels from the trainingSet
trainingLabels = trainingSet.Labels;

% Train multiclass SVM classifier using a fast linear solver, and set
% 'ObservationsIn' to 'columns' to match the arrangement used for training
% features.
classifier = fitcecoc(trainingFeatures, trainingLabels, ...
    'Learners', 'Linear', 'Coding', 'onevsall', 'ObservationsIn', 'columns');


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
    if ~islogical(img)
    img=im2bw(img);
    end
    Test_Image{id-2}=img;
    File_Image{id-2}=filename;
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    ds = augmentedImageDatastore(imageSize, RGBImage, 'ColorPreprocessing', 'gray2rgb');
    %ds = augmentedImageDatastore(imageSize, newImage);
     % Extract image features using the CNN
    imageFeatures = activations(net, ds, featureLayer, 'OutputAs', 'columns');
    %% Make a prediction using the SVM classifier
    svm_result_label{id-2}= predict(classifier, imageFeatures, 'ObservationsIn', 'columns');
    TestFeatures{id-2}=imageFeatures;
    %%classify using kNN
    %kNN_Result{id-2} = knnclassify(imageFeatures, trainingFeatures, trainingLabels);
end
toc;
























