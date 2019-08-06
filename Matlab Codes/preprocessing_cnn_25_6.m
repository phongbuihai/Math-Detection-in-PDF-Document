%%Preprocessing variable images
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\variable\';
clc;
testFolder = 'D:\NCKH\Nhandang_Congthuc\Bao_Co_Lan\DulieuThu\Train_set\33\';
Output_folder='D:\NCKH\Nhandang_Congthuc\Bao_Co_Lan\DulieuThu\Train_set_PNG\33\';
files =dir( fullfile(testFolder, '*.*') );%Doc cac file trong folder
allNames = { files.name };
%disp(allNames);
l=length(files);
w=45;
h=18;
fn='';
for id = 3:l
   % strcat(mathfolder,'\\');
   % filename=strcat(mathfolder,allNames(id));%get path and file name
    filename= fullfile(testFolder,allNames(id));
    filename=char(filename); %convert cell to string
    img=imread(filename);
   % img=imresize(img,[h w]);
    if ~islogical(img)
    img=im2bw(img);
    end
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    %Save image
        %fn=strcat('variable_',num2str(id-2));
        %fn=strcat(fn,'.png');
        %fn=strcat(num2str(count),'.png');
        fn=allNames{id};
        fn=strcat(Output_folder,fn);
       % fn= convertStringsToChars(fn)
        imwrite(RGBImage,char(fn));
        
end

%%Preprocessing text images
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\DeepLearning\text\';
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
Output_folder='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset_RGB\';
files =dir( fullfile(testFolder, '*.*') );%Doc cac file trong folder
allNames = { files.name };
%disp(allNames);
l=length(files);
w=45;
h=18;
fn='';
for id = 3:l
   % strcat(mathfolder,'\\');
   % filename=strcat(mathfolder,allNames(id));%get path and file name
    filename= fullfile(testFolder,allNames(id));
    filename=char(filename); %convert cell to string
    img=imread(filename);
   % img=imresize(img,[h w]);
    if ~islogical(img)
    img=im2bw(img);
    end
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    %Save image
        fn=strcat('text_',num2str(id-2));
        fn=strcat(fn,'.png');
        %fn=strcat(num2str(count),'.png');
        fn=strcat(Output_folder,fn);
       % fn= convertStringsToChars(fn)
        imwrite(RGBImage,char(fn));
        
end


















%%Preprocessing text images
testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset_RGB\';
%testFolder = 'D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\testset\';
Output_folder='D:\NCKH\Nhandang_Congthuc\Dataset\Marmot\marmot_math_formula_dataset_v1.0\Dataset\Training_Dataset\Testing_Word_26_12\Test_Variable_Index_5_10\ResNet_Test\test_224\';
files =dir( fullfile(testFolder, '*.*') );%Doc cac file trong folder
allNames = { files.name };
%disp(allNames);
l=length(files);
w=224;
h=224;
fn='';
for id = 3:l
   % strcat(mathfolder,'\\');
   % filename=strcat(mathfolder,allNames(id));%get path and file name
    filename= fullfile(testFolder,allNames(id));
    filename=char(filename); %convert cell to string
    img=imread(filename);
    img=imresize(img,[h w]);
    if ~islogical(img)
    img=im2bw(img);
    end
    % Create augmentedImageDatastore to automatically resize the image when
    % image features are extracted using activations.
    %repmat(newImage, 1, 1, 3);
    RGBImage = binary_rgb_image(img);%Convert binary image to RGB image 
    %Save image
        fn=strcat('test_',num2str(id-2));
        fn=strcat(fn,'.png');
        %fn=strcat(num2str(count),'.png');
        fn=strcat(Output_folder,fn);
       % fn= convertStringsToChars(fn)
        imwrite(RGBImage,char(fn));
        
end