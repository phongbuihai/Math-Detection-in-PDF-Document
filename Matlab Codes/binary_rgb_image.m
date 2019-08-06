 function RGBImage = binary_rgb_image(binaryImage) 
%binaryImage = imread(fullFileName);
[rows, columns, numberOfColorChannels] = size(binaryImage);
maxValue = max(binaryImage(:));
minValue = min(binaryImage(:));
if numberOfColorChannels > 1 || minValue < 0 || maxValue > 1
    message = sprintf('Image is not a binary Image!');
    uiwait(warndlg(message));
    return;
end
grayImage = 255 * uint8(binaryImage);
RGBImage = cat(3, grayImage, grayImage, grayImage);
%imshow(RGB);
 end
